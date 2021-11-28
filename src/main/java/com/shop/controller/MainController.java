package com.shop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController { //Home

    @GetMapping(value = "/")
    public String main(){
        return "main";
    }


}
