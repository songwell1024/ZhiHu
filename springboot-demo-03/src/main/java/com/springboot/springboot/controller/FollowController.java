package com.springboot.springboot.controller;


import com.springboot.springboot.async.EventModel;
import com.springboot.springboot.async.EventProducer;
import com.springboot.springboot.async.EventType;
import com.springboot.springboot.model.EntityType;
import com.springboot.springboot.model.HostHolder;
import com.springboot.springboot.model.Question;
import com.springboot.springboot.service.FollowService;
import com.springboot.springboot.service.questionService;
import com.springboot.springboot.service.userService;
import com.springboot.springboot.utils.WendaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author WilsonSong
 * @date 2018/6/22
 * 关注功能
 */
@Controller
public class FollowController {

    @Autowired
    FollowService followService;
    @Autowired
    userService uService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    EventProducer eventProducer;
    @Autowired
    questionService qService;

    //关注的人
    @RequestMapping(path = {"/followUser"}, method = {RequestMethod.POST})
    @ResponseBody
    public String followUser(@RequestParam("userId") int userId){
        if (hostHolder.getUser() == null){
            return "redirect:/reglogin";
        }

        //是否关注
        boolean ret = followService.follow(hostHolder.getUser().getId(), EntityType.ENTITY_USER, userId);

        //点击关注之后就触发相应的关注的事件
        eventProducer.fireEvent(new EventModel(EventType.FOLLOW)
                .setActorId(hostHolder.getUser().getId()).setEntityType(EntityType.ENTITY_USER)   //事件的触发者的id，触发者关注的对象属性是人
                .setEntityId(userId).setEntityOwnerId(userId));  //关注的对象的id，
        return WendaUtil.getJsonString(ret ? 0 : 1, String.valueOf(followService.getFolloweeCount(hostHolder.getUser().getId(),
                EntityType.ENTITY_USER)));  //我当前关注了多少人
    }

    //取消关注
    @RequestMapping(path = {"/unfollowUser"}, method = {RequestMethod.POST})
    @ResponseBody
    public String unfollowUser(@RequestParam("userId") int userId){
        if (hostHolder.getUser() == null){
            return "redirect:/reglogin";
        }

        //是否关注
        boolean ret = followService.unfollow(hostHolder.getUser().getId(), EntityType.ENTITY_USER, userId);

        //点击关注之后就触发相应的关注的事件
        eventProducer.fireEvent(new EventModel(EventType.UNFOLLOW)
                .setActorId(hostHolder.getUser().getId()).setEntityType(EntityType.ENTITY_USER)   //事件的触发者的id，触发者关注的对象属性是人
                .setEntityId(userId).setEntityOwnerId(userId));  //关注的对象的id，
        return WendaUtil.getJsonString(ret ? 0 : 1, String.valueOf(followService.getFolloweeCount(hostHolder.getUser().getId(),
                EntityType.ENTITY_USER)));  //我当前关注了多少人
    }

    //关注的问题
    @RequestMapping(path = {"/followQuestion"}, method = {RequestMethod.POST})
    @ResponseBody
    public String followQuestion(@RequestParam("questionId") int questionId){
        if (hostHolder.getUser() == null){
            return "redirect:/reglogin";
        }

        //问题可能不存在，需要先判断一下
        Question question = qService.selectQuestionById(questionId);
        if (question == null){
            return WendaUtil.getJsonString(1, "问题不存在");
        }
        //是否关注
        boolean ret = followService.follow(hostHolder.getUser().getId(), EntityType.ENTITY_QUESTION, questionId);

        //点击关注之后就触发相应的关注的事件
        eventProducer.fireEvent(new EventModel(EventType.FOLLOW)
                .setActorId(hostHolder.getUser().getId()).setEntityType(EntityType.ENTITY_QUESTION)   //事件的触发者的id，触发者关注的对象属性是人
                .setEntityId(questionId).setEntityOwnerId(questionId));  //关注的对象的id，
        return WendaUtil.getJsonString(ret ? 0 : 1, String.valueOf(followService.getFolloweeCount(hostHolder.getUser().getId(),
                EntityType.ENTITY_QUESTION)));  //我当前关注了多少人
    }

    //取消关注问题
    @RequestMapping(path = {"/unfollowQuestion"}, method = {RequestMethod.POST})
    @ResponseBody
    public String unfollowQuestion(@RequestParam("questionId") int questionId){
        if (hostHolder.getUser() == null){
            return "redirect:/reglogin";
        }

        Question question = qService.selectQuestionById(questionId);
        if (question == null){
            return WendaUtil.getJsonString(1, "问题不存在");
        }
        //是否关注
        boolean ret = followService.unfollow(hostHolder.getUser().getId(), EntityType.ENTITY_QUESTION, questionId);

        //点击关注之后就触发相应的关注的事件
        eventProducer.fireEvent(new EventModel(EventType.FOLLOW)
                .setActorId(hostHolder.getUser().getId()).setEntityType(EntityType.ENTITY_QUESTION)   //事件的触发者的id，触发者关注的对象属性是人
                .setEntityId(questionId).setEntityOwnerId(questionId));  //关注的对象的id，
        return WendaUtil.getJsonString(ret ? 0 : 1, String.valueOf(followService.getFolloweeCount(hostHolder.getUser().getId(),
                EntityType.ENTITY_QUESTION)));  //我当前关注了多少人
    }

}
