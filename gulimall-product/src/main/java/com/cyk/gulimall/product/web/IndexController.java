package com.cyk.gulimall.product.web;

import com.cyk.gulimall.product.entity.CategoryEntity;
import com.cyk.gulimall.product.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;
import java.util.List;

/**
 * The class IndexController.
 *
 * @author chenyukang
 * @email chen.yukang@qq.com
 * @date 2024/6/15
 **/
@Controller
public class IndexController {

    @Resource
    private CategoryService categoryService;

    @GetMapping(value = {"/","index.html"})
    private String indexPage(Model model) {

        //1、查出所有的一级分类
        List<CategoryEntity> categoryEntities = categoryService.getLevel1Categorys();
        model.addAttribute("categories",categoryEntities);

        return "index";
    }
}
