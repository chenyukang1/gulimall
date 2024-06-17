package com.cyk.gulimall.product.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * The class RedissonConfig.
 *
 * @author chenyukang
 * @email chen.yukang@qq.com
 * @date 2024/6/17
 **/
@Configuration
public class RedissonConfig {

    @Bean(destroyMethod="shutdown")
    public RedissonClient redisson() throws IOException {
        //1、创建配置
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://116.198.200.0:6379")
                .setPassword("123456");

        //2、根据Config创建出RedissonClient实例
        //Redis url should start with redis:// or rediss://
        return Redisson.create(config);
    }
}
