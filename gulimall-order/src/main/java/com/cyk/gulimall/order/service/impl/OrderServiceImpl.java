package com.cyk.gulimall.order.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cyk.common.exception.NoStockException;
import com.cyk.common.utils.PageUtils;
import com.cyk.common.utils.Query;
import com.cyk.common.utils.R;
import com.cyk.common.vo.MemberResponseVo;
import com.cyk.gulimall.order.constant.OrderConstant;
import com.cyk.gulimall.order.dao.OrderDao;
import com.cyk.gulimall.order.entity.OrderEntity;
import com.cyk.gulimall.order.entity.OrderItemEntity;
import com.cyk.gulimall.order.enume.OrderStatusEnum;
import com.cyk.gulimall.order.feign.MemberFeignService;
import com.cyk.gulimall.order.feign.ProductFeignService;
import com.cyk.gulimall.order.feign.WmsFeignService;
import com.cyk.gulimall.order.interceptor.LoginUserInterceptor;
import com.cyk.gulimall.order.service.OrderItemService;
import com.cyk.gulimall.order.service.OrderService;
import com.cyk.gulimall.order.service.PaymentInfoService;
import com.cyk.gulimall.order.to.OrderCreateTo;
import com.cyk.gulimall.order.to.SpuInfoVo;
import com.cyk.gulimall.order.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

import static com.cyk.common.constant.CartConstant.CART_PREFIX;


@Service("orderService")
@Slf4j
public class OrderServiceImpl extends ServiceImpl<OrderDao, OrderEntity> implements OrderService {

    @Autowired
    private MemberFeignService memberFeignService;

//    @Autowired
//    private CartFeignService cartFeignService;

    @Autowired
    private WmsFeignService wmsFeignService;

    @Autowired
    private ProductFeignService productFeignService;

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private PaymentInfoService paymentInfoService;

//    @Autowired
//    private BestPayService bestPayService;

    @Autowired
    private ThreadPoolExecutor threadPoolExecutor;

    private final ThreadLocal<OrderSubmitVo> confirmVoThreadLocal = new ThreadLocal<>();

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OrderEntity> page = this.page(
                new Query<OrderEntity>().getPage(params),
                new QueryWrapper<OrderEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 提交订单
     *
     * @param vo
     * @return
     */
    // @Transactional(isolation = Isolation.READ_COMMITTED) 设置事务的隔离级别
    // @Transactional(propagation = Propagation.REQUIRED)   设置事务的传播级别
    @Transactional(rollbackFor = Exception.class)
    // @GlobalTransactional(rollbackFor = Exception.class)
    @Override
    public SubmitOrderResponseVo submitOrder(OrderSubmitVo vo) {

        confirmVoThreadLocal.set(vo);

        SubmitOrderResponseVo responseVo = new SubmitOrderResponseVo();
        //去创建、下订单、验令牌、验价格、锁定库存...

        //获取当前用户登录的信息
        MemberResponseVo memberResponseVo = LoginUserInterceptor.loginUser.get();
        responseVo.setCode(0);

        //1、验证令牌是否合法【令牌的对比和删除必须保证原子性】
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        String orderToken = vo.getOrderToken();

        //通过lure脚本原子验证令牌和删除令牌
        Long result = redisTemplate.execute(new DefaultRedisScript<>(script, Long.class),
                Collections.singletonList(OrderConstant.USER_ORDER_TOKEN_PREFIX + memberResponseVo.getId()),
                orderToken);

        if (result == null || result == 0L) {
            //令牌验证失败
            responseVo.setCode(1);
            return responseVo;
        } else {
            //令牌验证成功
            //1、创建订单、订单项等信息
            OrderCreateTo order = createOrder();

            //2、验证价格
            BigDecimal payAmount = order.getOrder().getPayAmount();
            BigDecimal payPrice = vo.getPayPrice();

            if (Math.abs(payAmount.subtract(payPrice).doubleValue()) < 0.01) {
                //金额对比
                //TODO 3、保存订单
                saveOrder(order);

                //4、库存锁定,只要有异常，回滚订单数据
                //订单号、所有订单项信息(skuId,skuNum,skuName)
                WareSkuLockVo lockVo = new WareSkuLockVo();
                lockVo.setOrderSn(order.getOrder().getOrderSn());

                //获取出要锁定的商品数据信息
                List<OrderItemVo> orderItemVos = order.getOrderItems().stream().map((item) -> {
                    OrderItemVo orderItemVo = new OrderItemVo();
                    orderItemVo.setSkuId(item.getSkuId());
                    orderItemVo.setCount(item.getSkuQuantity());
                    orderItemVo.setTitle(item.getSkuName());
                    return orderItemVo;
                }).collect(Collectors.toList());
                lockVo.setLocks(orderItemVos);

                //TODO 调用远程锁定库存的方法
                //出现的问题：扣减库存成功了，但是由于网络原因超时，出现异常，导致订单事务回滚，库存事务不回滚(解决方案：seata)
                //为了保证高并发，不推荐使用seata，因为是加锁，并行化，提升不了效率,可以发消息给库存服务
                R r = wmsFeignService.orderLockStock(lockVo);
                if (r.getCode() == 0) {
                    //锁定成功
                    responseVo.setOrder(order.getOrder());
                    // int i = 10/0;

                    //TODO 订单创建成功，发送消息给MQ
                    rabbitTemplate.convertAndSend("order-event-exchange", "order.create.order", order.getOrder());

                    //删除购物车里的数据
                    redisTemplate.delete(CART_PREFIX + memberResponseVo.getId());
                    return responseVo;
                } else {
                    //锁定失败
                    String msg = (String) r.get("msg");
                    throw new NoStockException(msg);
                    // responseVo.setCode(3);
                    // return responseVo;
                }

            } else {
                responseVo.setCode(2);
                return responseVo;
            }
        }
    }

    private void saveOrder(OrderCreateTo orderCreateTo) {
        //获取订单信息
        OrderEntity order = orderCreateTo.getOrder();
        order.setModifyTime(new Date());
        order.setCreateTime(new Date());
        //保存订单
        this.baseMapper.insert(order);

        //获取订单项信息
        List<OrderItemEntity> orderItems = orderCreateTo.getOrderItems();
        //批量保存订单项数据
        orderItemService.saveBatch(orderItems);
    }

    private OrderCreateTo createOrder() {
        OrderCreateTo createTo = new OrderCreateTo();

        //1、生成订单号
        // todo 雪花算法生成订单号
        // todo 订单表分库分表
        // todo 基因法避免读请求扩散
        String orderSn = IdWorker.getTimeId();
        OrderEntity orderEntity = builderOrder(orderSn);

        //2、获取到所有的订单项
        List<OrderItemEntity> orderItemEntities = builderOrderItems(orderSn);

        //3、验价(计算价格、积分等信息)
        computePrice(orderEntity, orderItemEntities);

        createTo.setOrder(orderEntity);
        createTo.setOrderItems(orderItemEntities);

        return createTo;
    }

    private void computePrice(OrderEntity orderEntity, List<OrderItemEntity> orderItemEntities) {

    }

    private OrderEntity builderOrder(String orderSn) {
        //获取当前用户登录信息
        MemberResponseVo memberResponseVo = LoginUserInterceptor.loginUser.get();

        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setMemberId(memberResponseVo.getId());
        orderEntity.setOrderSn(orderSn);
        orderEntity.setMemberUsername(memberResponseVo.getUsername());

        OrderSubmitVo orderSubmitVo = confirmVoThreadLocal.get();

        //远程获取收货地址和运费信息
        R fareAddressVo = wmsFeignService.getFare(orderSubmitVo.getAddrId());
        FareVo fareResp = fareAddressVo.getData("data", new TypeReference<FareVo>() {
        });

        //获取到运费信息
        BigDecimal fare = fareResp.getFare();
        orderEntity.setFreightAmount(fare);

        //获取到收货地址信息
        MemberAddressVo address = fareResp.getAddress();
        //设置收货人信息
        orderEntity.setReceiverName(address.getName());
        orderEntity.setReceiverPhone(address.getPhone());
        orderEntity.setReceiverPostCode(address.getPostCode());
        orderEntity.setReceiverProvince(address.getProvince());
        orderEntity.setReceiverCity(address.getCity());
        orderEntity.setReceiverRegion(address.getRegion());
        orderEntity.setReceiverDetailAddress(address.getDetailAddress());

        //设置订单相关的状态信息
        orderEntity.setStatus(OrderStatusEnum.CREATE_NEW.getCode());
        orderEntity.setAutoConfirmDay(7);
        orderEntity.setConfirmStatus(0);
        return orderEntity;
    }


    private List<OrderItemEntity> builderOrderItems(String orderSn) {
        List<OrderItemEntity> orderItemEntityList = new ArrayList<>();

        //最后确定每个购物项的价格
//        List<OrderItemVo> currentCartItems = cartFeignService.getCurrentCartItems();
//        if (currentCartItems != null && !currentCartItems.isEmpty()) {
//            orderItemEntityList = currentCartItems.stream().map((items) -> {
//                //构建订单项数据
//                OrderItemEntity orderItemEntity = builderOrderItem(items);
//                orderItemEntity.setOrderSn(orderSn);
//
//                return orderItemEntity;
//            }).collect(Collectors.toList());
//        }

        return orderItemEntityList;
    }

    private OrderItemEntity builderOrderItem(OrderItemVo items) {

        OrderItemEntity orderItemEntity = new OrderItemEntity();

        //1、商品的spu信息
        Long skuId = items.getSkuId();
        //获取spu的信息
        R spuInfo = productFeignService.getSpuInfoBySkuId(skuId);
        SpuInfoVo spuInfoData = spuInfo.getData("data", new TypeReference<SpuInfoVo>() {
        });
        orderItemEntity.setSpuId(spuInfoData.getId());
        orderItemEntity.setSpuName(spuInfoData.getSpuName());
        orderItemEntity.setSpuBrand(spuInfoData.getBrandName());
        orderItemEntity.setCategoryId(spuInfoData.getCatalogId());

        //2、商品的sku信息
        orderItemEntity.setSkuId(skuId);
        orderItemEntity.setSkuName(items.getTitle());
        orderItemEntity.setSkuPic(items.getImage());
        orderItemEntity.setSkuPrice(items.getPrice());
        orderItemEntity.setSkuQuantity(items.getCount());

        //使用StringUtils.collectionToDelimitedString将list集合转换为String
        String skuAttrValues = StringUtils.collectionToDelimitedString(items.getSkuAttrValues(), ";");
        orderItemEntity.setSkuAttrsVals(skuAttrValues);

        //3、商品的优惠信息

        //4、商品的积分信息
        orderItemEntity.setGiftGrowth(items.getPrice().multiply(new BigDecimal(items.getCount())).intValue());
        orderItemEntity.setGiftIntegration(items.getPrice().multiply(new BigDecimal(items.getCount())).intValue());

        //5、订单项的价格信息
        orderItemEntity.setPromotionAmount(BigDecimal.ZERO);
        orderItemEntity.setCouponAmount(BigDecimal.ZERO);
        orderItemEntity.setIntegrationAmount(BigDecimal.ZERO);

        //当前订单项的实际金额.总额 - 各种优惠价格
        //原来的价格
        BigDecimal origin = orderItemEntity.getSkuPrice().multiply(new BigDecimal(orderItemEntity.getSkuQuantity().toString()));
        //原价减去优惠价得到最终的价格
        BigDecimal subtract = origin.subtract(orderItemEntity.getCouponAmount())
                .subtract(orderItemEntity.getPromotionAmount())
                .subtract(orderItemEntity.getIntegrationAmount());
        orderItemEntity.setRealAmount(subtract);

        return orderItemEntity;
    }
}