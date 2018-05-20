package com.springboot.springboot.controller;

import com.springboot.springboot.model.Question;
import com.springboot.springboot.model.viewObject;
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

import java.util.ArrayList;
import java.util.List;

@Controller
public class homeController {
    private static final Logger logger = LoggerFactory.getLogger(homeController.class);

    @Autowired
    questionService qService;

    @Autowired
    userService uService;

    @RequestMapping(path = {"/user/{userId}","/index"},method = RequestMethod.GET)
    public String userIndex(Model model, @PathVariable("userId") int userId){

        model.addAttribute("vos",getQuestions(userId,0,10));
        return "index";
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
            vo.set("user", uService.getUser(question.getUserId()));
            vos.add(vo);
        }
        return vos;
    }

}
