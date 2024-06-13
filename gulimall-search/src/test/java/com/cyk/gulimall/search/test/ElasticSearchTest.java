package com.cyk.gulimall.search.test;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * The class ElasticSearchTest.
 *
 * @author chenyukang
 * @email chen.yukang@qq.com
 * @date 2024/6/13
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class ElasticSearchTest {

    @Autowired
    RestHighLevelClient restHighLevelClient;

    @Test
    public void contextLoads() {
        log.info("{}", restHighLevelClient);
    }
}
