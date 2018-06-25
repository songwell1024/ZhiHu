package com.springboot.springboot.controller;


import com.springboot.springboot.async.EventModel;
import com.springboot.springboot.async.EventProducer;
import com.springboot.springboot.async.EventType;
import com.springboot.springboot.model.*;
import com.springboot.springboot.service.CommentService;
import com.springboot.springboot.service.FollowService;
import com.springboot.springboot.service.questionService;
import com.springboot.springboot.service.userService;
import com.springboot.springboot.utils.WendaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Autowired
    CommentService commentService;

    //关注功能
    @RequestMapping(path = {"/followUser"}, method = {RequestMethod.POST})
    @ResponseBody
    public String followUser(@RequestParam("userId") int userId){
        if (hostHolder.getUser() == null){
            return "redirect:/reglogin";
        }

        //是否关注
        boolean ret = followService.follow(hostHolder.getUser().getId(), userId, EntityType.ENTITY_USER);

        //点击关注之后就触发相应的关注的事件
        eventProducer.fireEvent(new EventModel(EventType.FOLLOW)
                .setActorId(hostHolder.getUser().getId()).setEntityType(EntityType.ENTITY_USER)   //事件的触发者的id，触发者关注的对象属性是人
                .setEntityId(userId).setEntityOwnerId(userId));  //关注的对象的id，
        return WendaUtil.getJsonString(ret ? 0 : 1, String.valueOf(followService.getFolloweeCount(hostHolder.getUser().getId(),
                EntityType.ENTITY_USER)));  //我当前关注了多少人
    }

    //取消关注功能
    @RequestMapping(path = {"/unfollowUser"}, method = {RequestMethod.POST})
    @ResponseBody
    public String unfollowUser(@RequestParam("userId") int userId){
        if (hostHolder.getUser() == null){
            return "redirect:/reglogin";
        }

        //是否关注
        boolean ret = followService.unfollow(hostHolder.getUser().getId(), userId, EntityType.ENTITY_USER);

//        //点击关注之后就触发相应的关注的事件
        //取消关注不需要触发相应的事件了，不需要事件处理和发送系统通知
//        eventProducer.fireEvent(new EventModel(EventType.UNFOLLOW)
//                .setActorId(hostHolder.getUser().getId()).setEntityType(EntityType.ENTITY_USER)   //事件的触发者的id，触发者关注的对象属性是人
//                .setEntityId(userId).setEntityOwnerId(userId));  //关注的对象的id，
        return WendaUtil.getJsonString(ret ? 0 : 1, String.valueOf(followService.getFolloweeCount(hostHolder.getUser().getId(),
                EntityType.ENTITY_USER)));  //我当前关注了多少人
    }

    //关注问题
    @RequestMapping(path = {"/followQuestion"}, method = {RequestMethod.POST})
    @ResponseBody
    public String followQuestion(@RequestParam("questionId") int questionId){
        if (hostHolder.getUser() == null){
            return "redirect:/reglogin";
        }

        //问题可能不存在，需要先判断一下
        Question question = qService.selectQuestionById(questionId);
        if (question == null){
            return WendaUtil.getJsonString(1, "该问题不存在");
        }
        //是否关注
        boolean ret = followService.follow(hostHolder.getUser().getId(), questionId, EntityType.ENTITY_QUESTION);

        //点击关注之后就触发相应的关注的事件
        eventProducer.fireEvent(new EventModel(EventType.FOLLOW)
                .setActorId(hostHolder.getUser().getId()).setEntityType(EntityType.ENTITY_QUESTION)   //事件的触发者的id，触发者关注的对象属性是人
                .setEntityId(questionId).setEntityOwnerId(question.getUserId()));  //关注的对象的id，

        //与前端交互的信息
        Map<String, Object> info = new HashMap<>();
        info.put("headURL", hostHolder.getUser().getHead_url());
        info.put("name", hostHolder.getUser().getName());
        info.put("id", hostHolder.getUser().getId());
        info.put("count", followService.getFollowerCount(questionId, EntityType.ENTITY_QUESTION));

        return WendaUtil.getJsonString(ret ? 0 : 1, info);  //我当前关注的问题
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
        boolean ret = followService.unfollow(hostHolder.getUser().getId(), questionId, EntityType.ENTITY_QUESTION);

//        //点击关注之后就触发相应的关注的事件
//        eventProducer.fireEvent(new EventModel(EventType.UNFOLLOW)
//                .setActorId(hostHolder.getUser().getId()).setEntityType(EntityType.ENTITY_QUESTION)   //事件的触发者的id，触发者关注的对象属性是人
//                .setEntityId(questionId).setEntityOwnerId(question.getUserId()));  //关注的对象的id，
        //与前端交互的信息
        Map<String, Object> info = new HashMap<>();
        info.put("head_url", hostHolder.getUser().getHead_url());
        info.put("name", hostHolder.getUser().getName());
        info.put("id", hostHolder.getUser().getId());
        info.put("count", followService.getFollowerCount(questionId, EntityType.ENTITY_QUESTION));

        return WendaUtil.getJsonString(ret ? 0 : 1, info);  //我当前关注了多少人
    }


    //关注的人有哪些
    @RequestMapping(path = {"/user/{uid}/followees"}, method = {RequestMethod.GET})
    public String followees(Model model, @PathVariable("uid") int userId){
        List<Integer> followeesIds = followService.getFollowees(userId, EntityType.ENTITY_USER, 0, 10);      //关注的人的列表，分页显示10个
        if (hostHolder.getUser() != null){
            model.addAttribute("followees", getUserInfo(hostHolder.getUser().getId(), followeesIds));

        }else {
            model.addAttribute("followees", getUserInfo(0, followeesIds));
        }
        model.addAttribute("followeeCount", followService.getFolloweeCount(userId,EntityType.ENTITY_USER));
        model.addAttribute("curUser", uService.getUser(userId));
        return "followees";
    }

    //关注我的人有哪些，粉丝
    @RequestMapping(path = {"/user/{uid}/followers"}, method = {RequestMethod.GET})
    public String followers(Model model, @PathVariable("uid") int userId){
        List<Integer> followersIds = followService.getFollowers(userId, EntityType.ENTITY_USER, 0, 10);      //关注的人的列表，分页显示10个
        if (hostHolder.getUser() != null){
            model.addAttribute("followers", getUserInfo(hostHolder.getUser().getId(), followersIds));
        }else {
            model.addAttribute("followers",getUserInfo(0,followersIds));
        }
        model.addAttribute("followerCount", followService.getFollowerCount(userId, EntityType.ENTITY_USER));
        model.addAttribute("curUser", uService.getUser(userId));
        return "followers";

    }

    private List<viewObject> getUserInfo(int localUserId, List<Integer> userIds){
        List<viewObject> userInfo = new ArrayList<>();
        for (int uid :  userIds){
            User user = uService.getUser(uid);
            if (user == null){
                continue;
            }
            viewObject vo = new viewObject();
            vo.set("user", user);
            vo.set("commentCount", commentService.getUserCommentCount(uid));
            vo.set("followeeCount", followService.getFolloweeCount(uid, EntityType.ENTITY_USER));
            vo.set("followerCount", followService.getFollowerCount(uid,EntityType.ENTITY_USER));
            if (localUserId != 0){
                vo.set("followed", followService.isFollower(localUserId,uid,EntityType.ENTITY_USER));
            }else {
                vo.set("followed", false);
            }
            userInfo.add(vo);
        }
        return userInfo;
    }

}
