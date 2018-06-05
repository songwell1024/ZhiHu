package com.springboot.springboot.controller;

import com.springboot.springboot.async.EventModel;
import com.springboot.springboot.async.EventProducer;
import com.springboot.springboot.async.EventType;
import com.springboot.springboot.service.userService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

//首页的登录功能
@Controller
public class registerController {
    private static final Logger logger = LoggerFactory.getLogger(registerController.class);

    @Autowired
    userService uService;

    @Autowired
    EventProducer eventProducer;

    //注册
    @RequestMapping(path = {"/reg/"}, method = {RequestMethod.POST})
    public String reg(Model model, @RequestParam("username") String username, @RequestParam("password") String password,
                      @RequestParam(value = "next",defaultValue = "false") String next,
                      @RequestParam(value = "rememberme",defaultValue = "false") boolean rememberme,
                      HttpServletResponse response) {
        try {
            Map<String, String> map = uService.register(username, password);
            if (map.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket",map.get("ticket").toString());
                cookie.setPath("/");
                response.addCookie(cookie);
                if(!StringUtils.isEmpty(next)&&!("false".equals(next))){           //不为空的时候跳转
                    return "redirect:"+next;
                }
                return "redirect:/";
            }else{
                model.addAttribute("msg", map.get("msg"));
                return "login";
            }
        } catch (Exception e) {
            logger.error("注册异常" + e.getMessage());
            return "login";
        }
    }

    @RequestMapping(path = {"/reglogin"}, method = {RequestMethod.GET})
    public String register(Model model,@RequestParam(value = "next",defaultValue = "false") String next) {
        model.addAttribute("next",next);
        return "login";
    }

    //登陆
    @RequestMapping(path={"/login/"},method = {RequestMethod.POST})
    public String login(Model model,@RequestParam("username") String username, @RequestParam("password") String password,
                        @RequestParam(value = "next",defaultValue = "false") String next,
                        @RequestParam(value = "rememberme",defaultValue = "false") boolean rememberme,
                        HttpServletResponse response){
        try{
            Map<String,Object> map = uService.login(username,password);
            if(map.containsKey("ticket")){
                Cookie cookie = new Cookie("ticket",map.get("ticket").toString());
                cookie.setPath("/");           //可在同一应用服务器内共享cookie
                response.addCookie(cookie);

                //用户登陆完设置一个事件
                eventProducer.fireEvent(new EventModel(EventType.LOGIN)
                        .setExts("username",username).setExts("email", "1390505180@qq.com"));
                       // .setActorId((int)map.get("user_id")));          //用户登陆完之后判断是谁登录的
                //当读取到的next字段不为空的话跳转
               if(!StringUtils.isEmpty(next) && !("false".equals(next))){
                   return "redirect:"+ next ;
               }
               return "redirect:/";
            }
            else{
                model.addAttribute("msg",map.get("msg"));
                return "login";
            }
        }catch (Exception e){
            logger.error("登陆异常" + e.getMessage());
            return "login";
        }
    }

    @RequestMapping(path={"/logout"},method = {RequestMethod.GET})
    public String logout(@CookieValue("ticket") String ticket){
        uService.logout(ticket);
        return "redirect:/";
    }

}
