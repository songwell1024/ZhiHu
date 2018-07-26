package com.springboot.springboot.interceptor;

import com.springboot.springboot.dao.loginTicketsDAO;
import com.springboot.springboot.dao.userDAO;
import com.springboot.springboot.model.HostHolder;
import com.springboot.springboot.model.User;
import com.springboot.springboot.model.loginTickets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * 拦截器
 * @ 用来判断用户的
 *1. 当preHandle方法返回false时，从当前拦截器往回执行所有拦截器的afterCompletion方法，再退出拦截器链。也就是说，请求不继续往下传了，直接沿着来的链往回跑。
 2.当preHandle方法全为true时，执行下一个拦截器,直到所有拦截器执行完。再运行被拦截的Controller。然后进入拦截器链，运
 行所有拦截器的postHandle方法,完后从最后一个拦截器往回执行所有拦截器的afterCompletion方法.
 */

//这是第二个拦截器
//@component （把普通pojo实例化到spring容器中，相当于配置文件中的
@Component
public class LoginRequiredInterceptor implements HandlerInterceptor{

    @Autowired
    HostHolder hostHolder;

    //判断然后进行用户拦截
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //如果说现在是没有用户登录的
       if(hostHolder.getUser() == null){
            response.sendRedirect("/reglogin?next=" + request.getRequestURI());
//            return false;
       }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }
}
