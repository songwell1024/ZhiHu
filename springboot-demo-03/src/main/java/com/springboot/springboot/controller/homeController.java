package com.springboot.springboot.controller;

import com.springboot.springboot.model.*;
import com.springboot.springboot.service.CommentService;
import com.springboot.springboot.service.FollowService;
import com.springboot.springboot.service.questionService;
import com.springboot.springboot.service.userService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.swing.text.html.parser.Entity;
import java.util.ArrayList;
import java.util.List;

@Controller
public class homeController {
    private static final Logger logger = LoggerFactory.getLogger(homeController.class);

    @Autowired
    questionService qService;

    @Autowired
    userService uService;

    @Autowired
    FollowService followService;
    @Autowired
    CommentService commentService;
    @Autowired
    HostHolder hostHolder;

    @RequestMapping(path = {"/user/{userId}","/index"},method = RequestMethod.GET)
    public String userIndex(Model model, @PathVariable("userId") int userId){

        model.addAttribute("vos",getQuestions(userId,0,10));

        //显示关注和被关注列表
        User user = uService.getUser(userId);
        viewObject vo = new viewObject();
        vo.set("user", user);
        vo.set("commentCount", commentService.getUserCommentCount(userId));
        vo.set("followeeCount", followService.getFolloweeCount(userId,EntityType.ENTITY_USER));
        vo.set("followerCount", followService.getFollowerCount(userId,EntityType.ENTITY_USER));
        if (hostHolder.getUser() != null){
            vo.set("followed", followService.isFollower(hostHolder.getUser().getId(),userId, EntityType.ENTITY_USER));
        }else {
            vo.set("followed", false);
        }
        model.addAttribute("profileUser", vo);
        return "profile";
    }

    @RequestMapping(path = {"/","/index"},method = RequestMethod.GET)
    public String home(Model model){

        model.addAttribute("vos",getQuestions(0,0,10));
        return "index";
    }

    private List<viewObject> getQuestions(int userId,int offset, int limit){
        List<Question> questionList = qService.selectLatestQuestions(userId,offset,limit);
        List<viewObject> vos = new ArrayList<>();
        for (Question question:questionList){
            viewObject vo = new viewObject();
            vo.set("question",question);
            //问题关注的数量
            vo.set("followCount", followService.getFollowerCount(question.getId(),EntityType.ENTITY_QUESTION));
            vo.set("user", uService.getUser(question.getUserId()));
            vos.add(vo);
        }
        return vos;
    }

}
