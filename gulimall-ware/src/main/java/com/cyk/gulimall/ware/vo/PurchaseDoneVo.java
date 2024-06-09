package com.cyk.gulimall.ware.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * The class PurchaseDoneVo.
 *
 * @author chenyukang
 * @email chen.yukang@qq.com
 * @date 2024/6/9
 **/
@Data
public class PurchaseDoneVo {

    @NotNull(message = "id不允许为空")
    private Long id;

    private List<PurchaseItemDoneVo> items;

}
