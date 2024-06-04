package com.cyk.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cyk.common.utils.PageUtils;
import com.cyk.gulimall.product.entity.SpuInfoEntity;

import java.util.Map;

/**
 * spu信息
 *
 * @author chenyk
 * @email 18061446901@163.com
 * @date 2024-06-04 20:30:17
 */
public interface SpuInfoService extends IService<SpuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

