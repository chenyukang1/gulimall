package com.cyk.gulimall.search.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RequestOptions;
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

    public static final RequestOptions COMMON_OPTIONS;

    static {
        // RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder().build() ;
        // builder.addHeader("Authorization", "Bearer " + TOKEN);
        // builder.setHttpAsyncResponseConsumerFactory(
        //         new HttpAsyncResponseConsumerFactory
        //                 .HeapBufferedResponseConsumerFactory(30 * 1024 * 1024 * 1024));
        COMMON_OPTIONS = RequestOptions.DEFAULT.toBuilder().build();
    }

    @Bean
    public RestHighLevelClient esRestClient(ElasticSearchConfigProperties properties) {
        return new RestHighLevelClient(
                RestClient.builder(new HttpHost(properties.getHostname(),
                        properties.getPort(), properties.getScheme())));
    }
}
