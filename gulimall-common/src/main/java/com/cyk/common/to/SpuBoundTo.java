package com.cyk.common.to;

import lombok.Data;

import java.math.BigDecimal;

/**
 * The class SpuBoundTo.
 *
 * @author chenyukang
 * @email chen.yukang@qq.com
 * @date 2024/6/9
 **/
@Data
public class SpuBoundTo {

    private Long spuId;

    private BigDecimal buyBounds;

    private BigDecimal growBounds;

}
