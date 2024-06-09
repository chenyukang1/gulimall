package com.cyk.gulimall.ware.vo;

import lombok.Data;

import java.util.List;

/**
 * The class MergeVo.
 *
 * @author chenyukang
 * @email chen.yukang@qq.com
 * @date 2024/6/9
 **/
@Data
public class MergeVo {

    private Long purchaseId;

    private List<Long> items;

}
