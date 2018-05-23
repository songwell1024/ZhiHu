package com.springboot.springboot.controller;

import com.springboot.springboot.model.User;
import com.springboot.springboot.service.WendaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class indexController {

    private static final Logger logger = LoggerFactory.getLogger(indexController.class);

    @Autowired
    WendaService wendaService;

    @RequestMapping("/")
    @ResponseBody  //因为引入了freemaker模板解析，所有当没有模板的时候需要加上ResponseBody,来证明你的访问的不是页面，只是返回字符串
    public String index(HttpSession session){
        logger.info("index");
        return "This is a spring-boot demo" + session.getAttribute("msg ") + wendaService.getMessage(2);
    }
//    @RequestMapping(value = "/index")
//    @RequestMapping(path = {"/","/index"})
//    @ResponseBody
//    public String index(){
//        return "This is a Spring Boot demo";
//    }

//    @RequestMapping("/index/{userId}")
//    @ResponseBody
//    public String index(@PathVariable("userId") Integer userId,
//                        @RequestParam( value = "type", defaultValue = "qq",required = false) String type){
//        return String.format("This is a spring-boot demo %d %s" ,userId,type);
//    }

    @RequestMapping("/home")
    public String index(Model model){
        model.addAttribute("value1","aaa");
        List<String> colors = Arrays.asList(new String[] {"green","red","blue"});
        model.addAttribute("Colors",colors);

        Map<String,String> maps = new HashMap<>();
        for (int i=0;i<3;++i){
            maps.put(String.valueOf(i),String.valueOf(i*i));
        }
        model.addAttribute("maps",maps);
        //model.addAttribute("User",new User("Lee"));
        return "home";
    }

    @RequestMapping("/request")
    @ResponseBody
    public String request(Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session){
        StringBuffer sb = new StringBuffer();

//        Iterator iterator = (Iterator) request.getHeaderNames();
//        while (iterator.hasNext()){
//            String name = (String)iterator.next();
//            sb.append(name +": " + request.getHeader(name) + "</br>");
//
//        }
        Enumeration<String> headerNames = request.getHeaderNames();
        while(headerNames.hasMoreElements()){
            String name = headerNames.nextElement();
            sb.append(name + ": "+ request.getHeader(name) + "</br>");

        }

        if(request.getCookies() != null){
            for(Cookie cookie : request.getCookies()){
                sb.append("Name: "+cookie.getName()+","+ "Value:" + cookie.getValue()+"</br>");
            }
        }

        sb.append(request.getMethod() + "</br>");
        sb.append(request.getRequestURI() + "</br>");

        return sb.toString();
    }

//    @RequestMapping("/redirect/{code}")
//    public String redirect(@PathVariable("code") int code){
//        return "redirect:/";
//    }
    //301跳转和302跳转的区别就是302是临时跳转，302是强制跳转，302转向可能会有URL规范化及网址劫持的问题
    @RequestMapping("/redirect/{code}")
    public RedirectView redirectView(@PathVariable("code") int code, HttpSession session){
        session.setAttribute("msg"," Jump from redirect");
        RedirectView redirectView = new RedirectView("/");     //跳转的路径
        if (code == 301){
            redirectView.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        }
        return redirectView;
    }

    @RequestMapping("/herror")
    @ResponseBody
    public String admin(@RequestParam("key") String key) {
        if("admin".equals(key)){
            return " back home";
        }
        throw new IllegalArgumentException("参数错误");
    }

    @ExceptionHandler()
    @ResponseBody
    public String error(Exception e){
        return "error:"+ e.getMessage();
    }

}
