package com.springboot.springboot.async.handler;


import com.springboot.springboot.async.EventHandler;
import com.springboot.springboot.async.EventModel;
import com.springboot.springboot.async.EventType;
import com.springboot.springboot.model.EntityType;
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
 * @date 2018/6/24
 * 点击关注引发的事件
 */
@Component
public class FollowHandler implements EventHandler {

    @Autowired
    MessageService messageService;
    @Autowired
    userService uService;

    //系统发送通知的事件
    @Override
    public void doHander(EventModel model) {
        Message message = new Message();
        message.setFrom_id(WendaUtil.SYSTEMCONTROLLER_userId);    //发送消息的是系统
        message.setTo_id(model.getEntityOwnerId());
        message.setConversationId(message.getConversationId());
        message.setCreated_date(new Date());
        User user = uService.getUser(model.getActorId());
        if (model.getEntityType() == EntityType.ENTITY_QUESTION){
            message.setContent("用户" + user.getName() + "关注了你的问题，http://127.0.0.1:8080/question/" + model.getEntityId());
        }else if(model.getEntityType() == EntityType.ENTITY_USER){
            message.setContent("用户"+user.getName() + "关注了你，http://127.0.0.1:8080/user/" + model.getActorId());
        }
        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.FOLLOW);               //支持的事件就是follow事件
    }
}
