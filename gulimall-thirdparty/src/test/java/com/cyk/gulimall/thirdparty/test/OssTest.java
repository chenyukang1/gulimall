package com.cyk.gulimall.thirdparty.test;

import com.aliyun.oss.OSS;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * The class OSSTest.
 *
 * @author chenyukang
 * @email chen.yukang@qq.com
 * @date 2024/6/6
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class OssTest {

    @Autowired
    private OSS oss;

    @Test
    public void testUpload() throws FileNotFoundException {
        FileInputStream inputStream = new FileInputStream("/Users/chenyk/Downloads/images.png");
        // 参数1位bucket  参数2位最终名字
        oss.putObject("gulimall-chenyk", "images.png", inputStream);
        oss.shutdown();
        log.info("上传成功");
    }
}
