package com.springboot.springboot.controller;

import com.springboot.springboot.model.EntityType;
import com.springboot.springboot.model.Question;
import com.springboot.springboot.model.viewObject;
import com.springboot.springboot.service.FollowService;
import com.springboot.springboot.service.SearchService;
import com.springboot.springboot.service.questionService;
import com.springboot.springboot.service.userService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

/**
 * 搜索
 * @author WilsonSong
 * @date 2018/7/25/025
 */
@Controller
public class SearchController {
    private static final Logger logger = LoggerFactory.getLogger(SearchController.class);

    @Autowired
    SearchService searchService;
    @Autowired
    questionService qService;
    @Autowired
    FollowService followService;
    @Autowired
    userService uService;

    @RequestMapping(path = {"/search"}, method = {RequestMethod.GET})
    public String Search(Model model,@RequestParam("q") String keyword, @RequestParam(value = "offset",defaultValue = "0") int offset
                         , @RequestParam(value = "count", defaultValue = "10") int count){
        try{
            List<Question> questionList = searchService.searchQuestion(keyword,offset,count, "<em>","</em>");
            List<viewObject> vos = new ArrayList<>();
            for (Question question:questionList){
                viewObject vo = new viewObject();
                vo.set("question",question);
                //问题关注的数量
                vo.set("followCount", followService.getFollowerCount(question.getId(), EntityType.ENTITY_QUESTION));
                vo.set("user", uService.getUser(question.getUserId()));
                vos.add(vo);
            }
            model.addAttribute("vos", vos);
        }catch (Exception e){
            logger.error("查询失败" + e.getMessage());
        }
        return "result";
    }
}
