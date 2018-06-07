package com.springboot.springboot.async.handler;

import com.springboot.springboot.async.EventHandler;
import com.springboot.springboot.async.EventModel;
import com.springboot.springboot.async.EventType;
import com.springboot.springboot.utils.MailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * @author WilsonSong
 * @date 2018/6/5
 * 用来处理登陆异常的事件
 */
@Component  //在容器中自动注册
public class LoginExceptionHandler implements EventHandler {

    @Autowired
    MailSender mailSender;

    @Override
    public void doHander(EventModel model) {
        //判断发现用户登陆异常
        mailSender.sendWithHTMLTemplate(model.getExts("email"), model.getExts("username"),"登陆IP异常");
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LOGIN);    //这里只关心登陆这个事件
    }
}
