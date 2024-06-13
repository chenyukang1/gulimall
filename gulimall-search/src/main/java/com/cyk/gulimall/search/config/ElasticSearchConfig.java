package com.cyk.gulimall.search.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The class GulimallElasticSearchConfig.
 *
 * @author chenyukang
 * @email chen.yukang@qq.com
 * @date 2024/6/13
 **/
@Configuration
@EnableConfigurationProperties(ElasticSearchConfigProperties.class)
public class ElasticSearchConfig {

    @Bean
    public RestHighLevelClient esRestClient(ElasticSearchConfigProperties properties) {
        return new RestHighLevelClient(
                RestClient.builder(new HttpHost(properties.getHostname(),
                        properties.getPort(), properties.getScheme())));
    }
}
