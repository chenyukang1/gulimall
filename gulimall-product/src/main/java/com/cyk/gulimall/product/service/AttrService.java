package com.cyk.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cyk.common.utils.PageUtils;
import com.cyk.gulimall.product.entity.AttrEntity;

import java.util.Map;

/**
 * 商品属性
 *
 * @author chenyk
 * @email 18061446901@163.com
 * @date 2024-06-04 20:30:17
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

