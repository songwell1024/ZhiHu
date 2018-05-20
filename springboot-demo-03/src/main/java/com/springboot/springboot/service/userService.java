package com.springboot.springboot.service;

import com.springboot.springboot.dao.userDAO;
import com.springboot.springboot.model.User;
import com.springboot.springboot.utils.WendaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@Service
public class userService {
    Random random = new Random();

    @Autowired
    userDAO uDAO;

    public User getUser(int id){
        return uDAO.selectById(id);
    }

    public Map<String,String > register(String userName,String password){

        Map<String,String> map = new HashMap<String, String >();
        if(StringUtils.isEmpty(userName)){
            map.put("msg", "用户名不能为空");
            return map;
        }

        if(StringUtils.isEmpty(password)){
            map.put("msg","密码不能为空");
            return  map;
        }

        User user = uDAO.selectByName(userName);
        if(user != null){
            map.put("msg","用户名已被注册");
            return  map;
        }
        user = new User();
        user.setName(userName);
        user.setSalt(UUID.randomUUID().toString().substring(0,5));
        user.setHead_url(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
        user.setPassword(WendaUtil.MD5(password + user.getSalt()));
        uDAO.addUser(user);

        return map;
    }
}
