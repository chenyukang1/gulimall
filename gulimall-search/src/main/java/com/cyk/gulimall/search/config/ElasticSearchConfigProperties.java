package com.cyk.gulimall.search.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * The class ElasticSearchConfiguration.
 *
 * @author chenyukang
 * @email chen.yukang@qq.com
 * @date 2024/6/13
 **/
@Data
@ConfigurationProperties(prefix = "gulimall.es.config")
public class ElasticSearchConfigProperties {

    private String hostname;

    private Integer port;

    private String scheme;
}
