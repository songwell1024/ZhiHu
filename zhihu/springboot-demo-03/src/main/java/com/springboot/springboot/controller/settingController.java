package com.springboot.springboot.controller;

import com.springboot.springboot.service.WendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class settingController {
    @Autowired
    WendaService wendaService;

    @RequestMapping("/setting")
    @ResponseBody
    public String setting(){
        return "Setting is ok " + wendaService.getMessage(2);
    }
}
