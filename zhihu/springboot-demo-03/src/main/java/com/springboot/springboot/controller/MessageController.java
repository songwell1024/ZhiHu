package com.springboot.springboot.controller;

import com.springboot.springboot.model.HostHolder;
import com.springboot.springboot.model.Message;
import com.springboot.springboot.model.User;
import com.springboot.springboot.model.viewObject;
import com.springboot.springboot.service.MessageService;
import com.springboot.springboot.service.userService;
import com.springboot.springboot.utils.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author WilsonSong
 * @date 2018/5/31
 * 消息中心
 */
@Controller
public class MessageController {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    HostHolder hostHolder;

    @Autowired
    MessageService messageService;

    @Autowired
    userService uService;

    @RequestMapping(path = {"/msg/addMessage"}, method = RequestMethod.POST)
    @ResponseBody
    public String addMessage(@RequestParam("toName") String toName, @RequestParam("content") String content ){
        try{
            if (hostHolder.getUser() ==null){
                return WendaUtil.getJsonString(999,"发送失败");
            }

            User user = uService.selectUserByName(toName);
            if (user == null){
                WendaUtil.getJsonString(1,"用户不存在");
            }

            Message message = new Message();
            message.setContent(content);
            message.setCreated_date(new Date());
            message.setTo_id(user.getId());
            message.setFrom_id(hostHolder.getUser().getId());

            message.setConversationId(message.getConversationId());    //不知道他的为什么不用设置
            messageService.addMessage(message);
            return WendaUtil.getJsonString(0);     //0就是表示发送成功
        }catch (Exception e){
            logger.error("发送消息失败" + e.getMessage());
            return WendaUtil.getJsonString(1,"发送失败");
        }
    }

    //消息通知详情页
    @RequestMapping(path = {"msg/detail"},method = RequestMethod.GET)
    public String getConversationDetail(Model model, @RequestParam("conversation_id") String conversation_id){
        try{
            List<Message> messageList = messageService.getConversationDetail(conversation_id,0,10);
            List<viewObject> messages = new ArrayList<>();

            for (Message message :messageList){
                //直接跟新问题是否已读，如果当前用户是发送的用户就默认已读，如果是接收消息的用户就置为已读
                if(uService.getUser(message.getTo_id()).getId() == hostHolder.getUser().getId() ){
                    messageService.updateConversationReadStatus(message.getConversationId(),1);
                }

                viewObject vo = new viewObject();
                vo.set("message" , message);
                User user = uService.getUser(message.getFrom_id());
                if(user ==  null){
                    continue;
                }
                vo.set("head_url", user.getHead_url());
                vo.set("user_id", user.getId());
                messages.add(vo);

            }
            model.addAttribute("messages", messages);

        }catch (Exception e){
            logger.error("获取消息详情失败" + e.getMessage());
        }
        return "letterDetail";
    }

    @RequestMapping(path = {"/msg/list"},method = RequestMethod.GET)
    public String getConversationList(Model model){
        try{
            if(hostHolder.getUser() == null){
                return "redirect:/reglogin";
            }
            int localUserId = hostHolder.getUser().getId();
            List<viewObject> conversations  = new ArrayList<>();
            List<Message> conversationList = messageService.getConversationList(localUserId,0,10);

            for (Message message : conversationList){
                viewObject vo = new viewObject();
                vo.set("conversation",message);
                int targetId = message.getFrom_id() == localUserId ? message.getTo_id(): message.getFrom_id();
                User user = uService.getUser(targetId);
                vo.set("user", user);
                vo.set("unread",messageService.getUnreadConversationCount(localUserId,message.getConversationId()));
                vo.set("conversation_id", message.getConversationId());
                conversations.add(vo);
            }
            model.addAttribute("conversations", conversations);
        }catch (Exception e){
            logger.error("获取消息列表失败" + e.getMessage());
        }
        return "letter";
    }
}
