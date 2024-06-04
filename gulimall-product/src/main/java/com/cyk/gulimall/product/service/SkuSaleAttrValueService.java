package com.cyk.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cyk.common.utils.PageUtils;
import com.cyk.gulimall.product.entity.SkuSaleAttrValueEntity;

import java.util.Map;

/**
 * sku销售属性&值
 *
 * @author chenyk
 * @email 18061446901@163.com
 * @date 2024-06-04 20:30:17
 */
public interface SkuSaleAttrValueService extends IService<SkuSaleAttrValueEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

