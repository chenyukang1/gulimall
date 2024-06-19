package com.cyk.gulimall.order.vo;

import com.cyk.gulimall.order.entity.OrderEntity;
import lombok.Data;

/**
 * The class SubmitOrderResponseVo.
 *
 * @author chenyukang
 * @email chen.yukang@qq.com
 * @date 2024/6/19
 **/
@Data
public class SubmitOrderResponseVo {

    private OrderEntity order;

    /** 错误状态码 **/
    private Integer code;
}
