package com.cyk.gulimall.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cyk.common.constant.WareConstant;
import com.cyk.common.utils.PageUtils;
import com.cyk.common.utils.Query;
import com.cyk.gulimall.ware.dao.PurchaseDao;
import com.cyk.gulimall.ware.entity.PurchaseDetailEntity;
import com.cyk.gulimall.ware.entity.PurchaseEntity;
import com.cyk.gulimall.ware.service.PurchaseDetailService;
import com.cyk.gulimall.ware.service.PurchaseService;
import com.cyk.gulimall.ware.service.WareSkuService;
import com.cyk.gulimall.ware.vo.MergeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("purchaseService")
public class PurchaseServiceImpl extends ServiceImpl<PurchaseDao, PurchaseEntity> implements PurchaseService {

    @Autowired
    private PurchaseDetailService purchaseDetailService;

    @Autowired
    private WareSkuService wareSkuService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageUnreceive(Map<String, Object> params) {
        QueryWrapper<PurchaseEntity> queryWrapper = new QueryWrapper<PurchaseEntity>()
                .eq("status",0).or().eq("status",1);

        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

    /**
     * 合并采购需求
     * @param mergeVo
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void mergePurchase(MergeVo mergeVo) {
        Long purchaseId = mergeVo.getPurchaseId();

        //没有选择任何【采购单】，将自动创建新单进行合并。
        if (purchaseId == null) {
            PurchaseEntity purchaseEntity = new PurchaseEntity();
            //设置采购单的默认状态
            purchaseEntity.setStatus(WareConstant.PurchaseStatusEnum.CREATED.getCode());

            purchaseEntity.setCreateTime(new Date());
            purchaseEntity.setUpdateTime(new Date());
            this.save(purchaseEntity);

            //获取新建采购单的id
            purchaseId = purchaseEntity.getId();

        }

        List<Long> items = mergeVo.getItems();

        //TODO 确认采购单状态是0,1才可以合并
        Collection<PurchaseDetailEntity> purchaseDetailEntities = purchaseDetailService.listByIds(items);

        purchaseDetailEntities.forEach((item) -> {
            if (!item.getStatus().equals(WareConstant.PurchaseDetailStatusEnum.CREATED.getCode())
                    && !item.getStatus().equals(WareConstant.PurchaseDetailStatusEnum.ASSIGNED.getCode())) {
                throw new IllegalArgumentException("正在采购，无法进行分配");
            }
        });

        Long finalPurchaseId = purchaseId;
        List<PurchaseDetailEntity> collect = items.stream().map(i -> {
            PurchaseDetailEntity purchaseDetailEntity = new PurchaseDetailEntity();
            purchaseDetailEntity.setId(i);
            purchaseDetailEntity.setPurchaseId(finalPurchaseId);
            purchaseDetailEntity.setStatus(WareConstant.PurchaseDetailStatusEnum.ASSIGNED.getCode());
            return purchaseDetailEntity;
        }).collect(Collectors.toList());

        //批量修改
        purchaseDetailService.updateBatchById(collect);

        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setId(purchaseId);
        purchaseEntity.setUpdateTime(new Date());
        this.updateById(purchaseEntity);
    }

}