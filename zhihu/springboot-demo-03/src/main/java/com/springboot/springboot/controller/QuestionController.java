package com.springboot.springboot.controller;

import com.springboot.springboot.async.EventModel;
import com.springboot.springboot.async.EventProducer;
import com.springboot.springboot.async.EventType;
import com.springboot.springboot.model.*;
import com.springboot.springboot.service.*;
import com.springboot.springboot.utils.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class QuestionController {
    private static final Logger logger = LoggerFactory.getLogger(Question.class);

    @Autowired
    questionService qservice;

    @Autowired
    userService uService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    CommentService commentService;

    @Autowired
    LikeService likeService;

    @Autowired
    FollowService followService;
    @Autowired
    EventProducer eventProducer;

    @RequestMapping(path = "/question/add",method = {RequestMethod.POST})
    @ResponseBody               //因为是弹框，所以这里用的是json的返回
    public String addQuestion(@RequestParam("title") String title, @RequestParam("content") String content){
          try{
              Question question = new Question();
              question.setContent(content);
              question.setTitle(title);
              question.setCreatedDate(new Date());
              //判断当前用户，若当前用户为空的话付给他一个默认的ID，让其有一定的权限
              //也就是游客模式
              if (hostHolder.getUser() == null){
                    question.setUserId(WendaUtil.ANONYMOUS_userId);
                    //WendaUtil.getJsonString(999);   //可以参照前端的代码， 当code为999的时候就返回主页面
              }else {
                  question.setUserId(hostHolder.getUser().getId());
              }
              if(qservice.addQuestion(question)>0){
                  //添加问题后就产生一个异步的事件，把问题增加进去，然后达到实时搜索的功能
                  eventProducer.fireEvent(new EventModel(EventType.ADD_QUESTION)
                          .setActorId(question.getUserId()).setEntityId(question.getId())
                          .setExts("title", question.getTitle()).setExts("content", question.getContent()));
                  return WendaUtil.getJsonString(0);
              }
          }catch(Exception e){
              logger.error("添加问题失败" + e.getMessage());
          }
          return WendaUtil.getJsonString(1,"添加问题失败");
    }

    @RequestMapping(path = {"/question/{qid}"},method = RequestMethod.GET)
    public String questionDetails(Model model, @PathVariable("qid") int qid){
        Question question = qservice.selectQuestionById(qid);
        model.addAttribute("question",question);
        model.addAttribute("user",uService.getUser(question.getUserId()));

        List<Comment> commentList = commentService.selectCommentByEntity(qid, EntityType.ENTITY_QUESTION);
        List<viewObject> comments = new ArrayList<>();

        for (Comment comment : commentList){
           viewObject vo = new viewObject();
           vo.set("comment",comment);

           //判断是否是我喜欢的
            if(hostHolder.getUser() == null){
                vo.set("liked", 0);
            }
            else {
                vo.set("liked", likeService.getLikeStatus(hostHolder.getUser().getId(),EntityType.ENTITY_COMMENT,comment.getId()));
            }

           vo.set("likeCount", likeService.getLikeCount(EntityType.ENTITY_COMMENT,comment.getId()));
           vo.set("user", uService.getUser(comment.getUser_id()));
           comments.add(vo);
        }

        model.addAttribute("comments",comments);

        //获取关注的问题信息
        List<Integer> users = followService.getFollowers(qid, EntityType.ENTITY_QUESTION, 20);
        List<viewObject> followers = new ArrayList<>();
        for (Integer userId : users){
            viewObject vo = new viewObject();
            User u = uService.getUser(userId);
            if (u == null){
                continue;
            }
            vo.set("name", u.getName());
            vo.set("head_url", u.getHead_url());
            vo.set("id", u.getId());
            followers.add(vo);
        }
        model.addAttribute("followUsers",followers);
        if (hostHolder.getUser() != null){
            model.addAttribute("followed", followService.isFollower(hostHolder.getUser().getId(),qid, EntityType.ENTITY_QUESTION));

        }else {
            model.addAttribute("followed", false);
        }
        return "detail";

    }


}
