package com.cyk.gulimall.order.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * The class HelloController.
 *
 * @author chenyukang
 * @email chen.yukang@qq.com
 * @date 2024/6/17
 **/
@Controller
public class HelloController {

    @GetMapping(value = "/{page}.html")
    public String listPage(@PathVariable("page") String page) {
        return page;
    }
}
