package com.springboot.springboot.model;

import org.springframework.stereotype.Component;

@Component
public class HostHolder {
    //本地线程变量，在每个线程中都有一份该变量的拷贝，因此有多个线程同时访问的时候也不会发生冲突
    private static ThreadLocal<User> users = new ThreadLocal<>();

    //获取当前线程对应的那个变量
    public User getUser(){
        return users.get();
    }

    public void setUser(User user){
        users.set(user);
    }

    public void clear(){
        users.remove();
    }
}
