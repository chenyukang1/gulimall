package com.cyk.gulimall.product.feign;

import com.cyk.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * The class WareFeignService.
 *
 * @author chenyukang
 * @email chen.yukang@qq.com
 * @date 2024/6/15
 **/
@FeignClient("gulimall-ware")
public interface WareFeignService {

    @PostMapping(value = "/ware/waresku/hasStock")
    R getSkuHasStock(@RequestBody List<Long> skuIds);
}
