package com.springboot.springboot.model;

import com.alibaba.fastjson.JSONObject;

import java.util.Date;

/**
 * @author WilsonSong
 * @date 2018/6/26
 * 新鲜事
 */
public class Feed {
    private int id;
    private int type;      //新鲜事的类型
    private int user_id;
    private Date created_date;
    //JSON 串的格式，可以存储各种各样的消息
    private String data;      //新鲜事的数据可能是评论，可能是问题
    private JSONObject dataJSON;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public Date getCreated_date() {
        return created_date;
    }

    public void setCreated_date(Date created_date) {
        this.created_date = created_date;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
        dataJSON = JSONObject.parseObject(data);
    }

    public String get(String key){
        return dataJSON == null ? null:dataJSON.getString(key);
    }
}
