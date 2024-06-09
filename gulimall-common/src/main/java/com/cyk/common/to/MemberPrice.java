package com.cyk.common.to;

import lombok.Data;

import java.math.BigDecimal;

/**
 * The class MemberPrice.
 *
 * @author chenyukang
 * @email chen.yukang@qq.com
 * @date 2024/6/9
 **/
@Data
public class MemberPrice {

    private Long id;
    private String name;
    private BigDecimal price;

}
