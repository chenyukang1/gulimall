package com.cyk.gulimall.product.dao;

import com.cyk.gulimall.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author chenyk
 * @email 18061446901@163.com
 * @date 2024-06-04 20:30:17
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
