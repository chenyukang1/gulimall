package com.cyk.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cyk.common.utils.PageUtils;
import com.cyk.gulimall.ware.entity.PurchaseEntity;
import com.cyk.gulimall.ware.vo.MergeVo;

import java.util.Map;

/**
 * 采购信息
 *
 * @author chenyk
 * @email chen.yukang@qq.com
 * @date 2024-06-05 00:16:50
 */
public interface PurchaseService extends IService<PurchaseEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPageUnreceive(Map<String, Object> params);

    void mergePurchase(MergeVo mergeVo);
}

