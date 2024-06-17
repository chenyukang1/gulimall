package com.cyk.gulimall.product.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * The class RedisTest.
 *
 * @author chenyukang
 * @email chen.yukang@qq.com
 * @date 2024/6/17
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class RedisTest {

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void test() {
        Boolean res = stringRedisTemplate.opsForValue().setIfAbsent("cyk", "cyk-1", Duration.ofSeconds(30));
        Map<String, String> map = new HashMap<String, String>() {
            {
                put("name", "cyk");
                put("age", "18");
                put("gender", "man");
            }
        };
        stringRedisTemplate.opsForHash().putAll("cyk-hash", map);

        Assert.assertEquals(Boolean.TRUE, res);


    }
}
