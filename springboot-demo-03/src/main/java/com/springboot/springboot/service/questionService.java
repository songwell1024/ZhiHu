package com.springboot.springboot.service;

import com.springboot.springboot.dao.questionDAO;
import com.springboot.springboot.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class questionService {
    @Autowired
    questionDAO qDAO;

    @Autowired
    SensitiveService sensitiveService;


    public List<Question> selectLatestQuestions(int user_id, int offset, int limit){
        return qDAO.selectLatestQuestions(user_id,offset,limit);
    }

    //添加问题
    public int addQuestion(Question question){
        //HTML代码过滤
        question.setContent(HtmlUtils.htmlEscape(question.getContent()));  //过滤html标签,就是把html语言进行转义
        question.setTitle(HtmlUtils.htmlEscape(question.getTitle()));
        //敏感词过滤
        question.setContent(sensitiveService.filter(question.getContent()));
        question.setTitle(sensitiveService.filter(question.getTitle()));

        return qDAO.addQuestion(question) > 0 ? question.getId() : 0;
    }

    public Question selectQuestionById(int id){
        return qDAO.selectQuestionById(id);
    }

    //更新评论的数量
    public int updateCommentCount(int id, int comment_count){
        return qDAO.updateCommentCount(id,comment_count);
    }
}


