package com.cyk.gulimall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cyk.common.to.SkuReductionTo;
import com.cyk.common.utils.PageUtils;
import com.cyk.gulimall.coupon.entity.SkuFullReductionEntity;

import java.util.Map;

/**
 * 商品满减信息
 *
 * @author chenyk
 * @email chen.yukang@qq.com
 * @date 2024-06-04 23:37:33
 */
public interface SkuFullReductionService extends IService<SkuFullReductionEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveSkuReduction(SkuReductionTo skuReductionTo);
}

