package com.cyk.gulimall.product.test;

import com.cyk.gulimall.product.entity.BrandEntity;
import com.cyk.gulimall.product.service.BrandService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class GulimallProductApplicationTests {

	@Autowired
	private BrandService brandService;

	@Test
	public void contextLoads() {
	}

	@Test
	public void testDAO() {
		BrandEntity brandEntity = new BrandEntity();
		brandEntity.setName("小米");
		brandService.save(brandEntity);
		log.info("保存成功");
	}
}
