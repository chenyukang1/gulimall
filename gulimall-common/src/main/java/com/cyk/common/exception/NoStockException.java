package com.cyk.common.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * The class NoStockException.
 *
 * @author chenyukang
 * @email chen.yukang@qq.com
 * @date 2024/6/18
 **/
public class NoStockException extends RuntimeException {

    @Getter
    @Setter
    private Long skuId;

    public NoStockException(Long skuId) {
        super("商品id："+ skuId + "库存不足！");
    }

    public NoStockException(String msg) {
        super(msg);
    }


}
