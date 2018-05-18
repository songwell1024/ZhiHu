package com.springboot.springboot.controller;

import com.springboot.springboot.service.questionService;
import com.springboot.springboot.service.userService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class homeController {
    private static final Logger logger = LoggerFactory.getLogger(homeController.class);

    @Autowired
    questionService qService;

    @Autowired
    userService uService;

    @RequestMapping(path = {"/","/index"},method = RequestMethod.GET)
    public String home(){
        return "index";
    }

}
