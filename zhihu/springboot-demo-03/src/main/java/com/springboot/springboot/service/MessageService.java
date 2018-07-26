package com.springboot.springboot.service;

import com.springboot.springboot.dao.MessageDAO;
import com.springboot.springboot.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author WilsonSong
 * @date 2018/5/31
 */
@Service
public class MessageService {

    @Autowired
    MessageDAO messageDAO;

    @Autowired
    SensitiveService sensitiveService;

    //增加
    public int addMessage(Message message){
        message.setContent(sensitiveService.filter(message.getContent()));
        return messageDAO.addMessage(message) >0 ? message.getId() : 0;
    }

    //获取消息中心的详情
    public List<Message> getConversationDetail(String conversation_id, int offset, int limit){
        return messageDAO.getConversationDetail(conversation_id,offset,limit);
    }

    //获取列表
    public List<Message> getConversationList(int user_id, int offset, int limit){
        return messageDAO.getConversationList(user_id, offset, limit);
    }

    //未读消息数量
    public int getUnreadConversationCount(int user_id, String conversation_id){
        return messageDAO.getUnreadConversationCount(user_id, conversation_id);
    }

    //更新未读消息的状态，更新为已读
    public int updateConversationReadStatus(String conversation_id, int has_read){
         return messageDAO.updateConversationReadStatus(conversation_id,has_read);
    }

}
