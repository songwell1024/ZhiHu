package com.springboot.springboot.service;

import com.springboot.springboot.dao.loginTicketsDAO;
import com.springboot.springboot.dao.userDAO;
import com.springboot.springboot.model.User;
import com.springboot.springboot.model.loginTickets;
import com.springboot.springboot.utils.WendaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class userService {
    Random random = new Random();

    @Autowired
    userDAO uDAO;

    @Autowired
    loginTicketsDAO lTicketsDAO;


    //注册
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

        //注册完成下发ticket之后自动登录
        String ticket = addLoginTicket(user.getId());
        map.put("ticket",ticket);

        return map;
    }

    //登陆
    public Map<String,Object> login(String username, String password){
        Map<String,Object> map = new HashMap<String,Object>();
        if(StringUtils.isEmpty(username)){
            map.put("msg","用户名不能为空");
            return map;
        }

        if(StringUtils.isEmpty(password)){
            map.put("msg","密码不能为空");
            return map;
        }

        User user = uDAO.selectByName(username);
        if (user == null){
            map.put("msg","用户名不存在");
            return map;
        }

        if (!WendaUtil.MD5(password+user.getSalt()).equals(user.getPassword())) {
            map.put("msg", "密码错误");
            return map;
        }

        String ticket = addLoginTicket(user.getId());
        map.put("ticket",ticket);
        return map;
    }

    public String addLoginTicket(int user_id){
        loginTickets ticket = new loginTickets();
        ticket.setUserId(user_id);
        Date nowDate = new Date();
        nowDate.setTime(3600*24*100 + nowDate.getTime());
        ticket.setExpired(nowDate);
        ticket.setStatus(0);
        ticket.setTicket(UUID.randomUUID().toString().replaceAll("_",""));
        lTicketsDAO.addTicket(ticket);
        return ticket.getTicket();

    }

    public void logout(String ticket){
        lTicketsDAO.updateStatus(ticket,1);
    }

    public User getUser(int id){
        return uDAO.selectById(id);
    }

    public  User selectUserByName(String name){
        return uDAO.selectByName(name);
    }
}

