package com.springboot.springboot.controller;

import com.springboot.springboot.model.*;
import com.springboot.springboot.service.CommentService;
import com.springboot.springboot.service.questionService;
import com.springboot.springboot.service.userService;
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
           vo.set("user", uService.getUser(comment.getUser_id()));
           comments.add(vo);
        }

        model.addAttribute("comments",comments);
        return "detail";

    }


}
