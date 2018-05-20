package com.springboot.springboot.controller;

import com.springboot.springboot.service.userService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

//首页的登录功能
@Controller
public class registerController {
    private static final Logger logger = LoggerFactory.getLogger(registerController.class);

    @Autowired
    userService uService;

    @RequestMapping(path = {"/reg/"}, method = RequestMethod.POST)
    public String reg(Model model, @RequestParam("username") String username, @RequestParam("password") String password) {
        try {
            Map<String, String> map = uService.register(username, password);
            if (map.containsKey("msg")) {
                model.addAttribute("msg", map.get("msg"));
                return "login";
            }

            return "redirect:/";
        } catch (Exception e) {
            logger.error("注册异常" + e.getMessage());
            return "login";
        }

    }

    @RequestMapping(path = {"/register"}, method = RequestMethod.GET)
    public String register(Model model) {
        return "login";
    }
}
