package com.arua.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller("/")
@RequestMapping("/")
public class ZhuColltroller {
    @RequestMapping(value = "/")
    public String shopcar(){
        return "thymeleaf/sss";
        //thymeleaf/listitem
    }
}
