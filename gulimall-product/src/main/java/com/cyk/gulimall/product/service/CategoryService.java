package com.cyk.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cyk.common.utils.PageUtils;
import com.cyk.gulimall.product.entity.CategoryEntity;

import java.util.Map;

/**
 * 商品三级分类
 *
 * @author chenyk
 * @email 18061446901@163.com
 * @date 2024-06-04 20:30:17
 */
public interface CategoryService extends IService<CategoryEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

