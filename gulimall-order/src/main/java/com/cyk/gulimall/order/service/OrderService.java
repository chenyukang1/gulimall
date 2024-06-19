package com.cyk.gulimall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cyk.common.utils.PageUtils;
import com.cyk.gulimall.order.entity.OrderEntity;
import com.cyk.gulimall.order.vo.OrderSubmitVo;
import com.cyk.gulimall.order.vo.SubmitOrderResponseVo;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * 订单
 *
 * @author chenyk
 * @email chen.yukang@qq.com
 * @date 2024-06-05 00:09:01
 */
public interface OrderService extends IService<OrderEntity> {

    PageUtils queryPage(Map<String, Object> params);

    // @Transactional(isolation = Isolation.READ_COMMITTED) 设置事务的隔离级别
    // @Transactional(propagation = Propagation.REQUIRED)   设置事务的传播级别
    @Transactional(rollbackFor = Exception.class)
    // @GlobalTransactional(rollbackFor = Exception.class)
    SubmitOrderResponseVo submitOrder(OrderSubmitVo vo);
}

