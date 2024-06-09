package com.cyk.gulimall.ware.vo;

import lombok.Data;

/**
 * The class PurchaseItemDoneVo.
 *
 * @author chenyukang
 * @email chen.yukang@qq.com
 * @date 2024/6/9
 **/
@Data
public class PurchaseItemDoneVo {

    private Long itemId;

    private Integer status;

    private String reason;

}
