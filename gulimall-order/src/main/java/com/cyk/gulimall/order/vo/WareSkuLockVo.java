package com.cyk.gulimall.order.vo;

import lombok.Data;

import java.util.List;

/**
 * The class WareSkuLockVo.
 *
 * @author chenyukang
 * @email chen.yukang@qq.com
 * @date 2024/6/19
 **/
@Data
public class WareSkuLockVo {

    private String orderSn;

    /** 需要锁住的所有库存信息 **/
    private List<OrderItemVo> locks;
}
