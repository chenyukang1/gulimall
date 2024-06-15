package com.cyk.gulimall.search.service;

import com.cyk.common.es.SkuEsModel;

import java.io.IOException;
import java.util.List;

/**
 * The class ProductSaveService.
 *
 * @author chenyukang
 * @email chen.yukang@qq.com
 * @date 2024/6/15
 **/
public interface ProductSaveService {
    boolean productStatusUp(List<SkuEsModel> skuEsModels) throws IOException;
}
