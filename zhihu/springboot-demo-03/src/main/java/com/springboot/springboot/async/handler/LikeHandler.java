package com.springboot.springboot.async.handler;

import com.springboot.springboot.async.EventHandler;
import com.springboot.springboot.async.EventModel;
import com.springboot.springboot.async.EventType;
import com.springboot.springboot.model.Message;
import com.springboot.springboot.model.User;
import com.springboot.springboot.service.MessageService;
import com.springboot.springboot.service.userService;
import com.springboot.springboot.utils.WendaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author WilsonSong
 * @date 2018/6/4
 * 处理点赞事件的handler
 */
@Component        //就是把普通的对象在spring容器中初始化
public class LikeHandler implements EventHandler {

    @Autowired
    MessageService messageService;

    @Autowired
    userService uService;

    @Override
    public void doHander(EventModel model) {
        Message message = new Message();
        message.setFrom_id(WendaUtil.SYSTEMCONTROLLER_userId);  //以系统管理员的额身份给你发消息说谁给你点了赞
        message.setTo_id(model.getEntityOwnerId());      //发给谁，就是那个entity拥有者的id
        message.setCreated_date(new Date());
        User user = uService.getUser(model.getActorId()); //触发这个事件的用户id
        message.setContent("用户" + user.getName() + "赞了你的评论,http://127.0.0.1:8080/question" + model.getExts("questionId"));
        message.setConversationId(message.getConversationId());

        messageService.addMessage(message);

    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LIKE);        //只需要返回点赞的事件即可
    }
}
