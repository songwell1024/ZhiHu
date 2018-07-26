package com.springboot.springboot.async;

/**
 * @author WilsonSong
 * @date 2018/6/3
 * 枚举类，就是事件的各种类型
 */
public enum EventType {
    LIKE(0), COMMENT(1), LOGIN(2), MAIL(3), FOLLOW(4), UNFOLLOW(5), ADD_QUESTION(6);

    private int value;
    EventType(int value){
        this.value = value;
    }
    public int getValue(){
        return value;
    }

}
