package com.springboot.springboot.service;

import com.springboot.springboot.dao.userDAO;
import com.springboot.springboot.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class userService {
    @Autowired
    userDAO uDAO;

    public User getUser(int id){
        return uDAO.selectById(id);
    }
}
