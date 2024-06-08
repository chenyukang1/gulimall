package com.cyk.gulimall.product.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cyk.gulimall.product.entity.AttrAttrgroupRelationEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 属性&属性分组关联
 * 
 * @author chenyk
 * @email chen.yukang@qq.com
 * @date 2024-06-04 20:30:17
 */
@Mapper
public interface AttrAttrgroupRelationDao extends BaseMapper<AttrAttrgroupRelationEntity> {

    void deleteBatchRelation(@Param("entities") List<AttrAttrgroupRelationEntity> entities);
}
