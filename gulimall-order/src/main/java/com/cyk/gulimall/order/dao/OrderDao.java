package com.cyk.gulimall.order.dao;

import com.cyk.gulimall.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author chenyk
 * @email chen.yukang@qq.com
 * @date 2024-06-05 00:09:01
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}
