package com.cyk.gulimall.order.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * The class FareVo.
 *
 * @author chenyukang
 * @email chen.yukang@qq.com
 * @date 2024/6/19
 **/
@Data
public class FareVo {

    private MemberAddressVo address;

    private BigDecimal fare;

}
