package com.cyk.gulimall.member.dao;

import com.cyk.gulimall.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author chenyk
 * @email chen.yukang@qq.com
 * @date 2024-06-04 23:46:36
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
