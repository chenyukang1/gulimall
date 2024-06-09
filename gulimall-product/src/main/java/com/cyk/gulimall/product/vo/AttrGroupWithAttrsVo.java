package com.cyk.gulimall.product.vo;

import com.cyk.gulimall.product.entity.AttrEntity;
import lombok.Data;

import java.util.List;

/**
 * The class AttrGroupWithAttrsVo.
 *
 * @author chenyukang
 * @email chen.yukang@qq.com
 * @date 2024/6/9
 **/
@Data
public class AttrGroupWithAttrsVo {

    /**
     * 分组id
     */
    private Long attrGroupId;
    /**
     * 组名
     */
    private String attrGroupName;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 描述
     */
    private String descript;
    /**
     * 组图标
     */
    private String icon;
    /**
     * 所属分类id
     */
    private Long catelogId;

    private List<AttrEntity> attrs;
}
