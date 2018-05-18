package com.springboot.springboot.service;

import com.springboot.springboot.dao.questionDAO;
import com.springboot.springboot.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class questionService {
    @Autowired
    questionDAO qDAO;
    public List<Question> selectLatestQuestions(int user_id,int offset, int limit){
        return qDAO.selectLatestQuestions(user_id,offset,limit);
    }
}
