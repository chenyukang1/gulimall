package com.cyk.gulimall.product.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * The class AttrRespVo.
 *
 * @author chenyukang
 * @email chen.yukang@qq.com
 * @date 2024/6/8
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class AttrRespVo extends AttrVo {

    private String catelogName;

    private String groupName;

    private Long[] catelogPath;

}
