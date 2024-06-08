package com.cyk.gulimall.product.vo;

import lombok.Data;

/**
 * The class AttrGroupRelationVo.
 *
 * @author chenyukang
 * @email chen.yukang@qq.com
 * @date 2024/6/8
 **/
@Data
public class AttrGroupRelationVo {

    //[{"attrId":1,"attrGroupId":2}]
    private Long attrId;

    private Long attrGroupId;
}
