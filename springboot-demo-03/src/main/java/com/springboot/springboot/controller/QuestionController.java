package com.springboot.springboot.controller;

import com.springboot.springboot.model.HostHolder;
import com.springboot.springboot.model.Question;
import com.springboot.springboot.service.questionService;
import com.springboot.springboot.utils.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@Controller
public class QuestionController {
    private static final Logger logger = LoggerFactory.getLogger(Question.class);

    @Autowired
    questionService qservice;

    @Autowired
    HostHolder hostHolder;

    @RequestMapping(value = "/question/add",method = {RequestMethod.POST})
    @ResponseBody
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



}
