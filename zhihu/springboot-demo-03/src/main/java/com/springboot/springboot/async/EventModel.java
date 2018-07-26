package com.springboot.springboot.async;

import java.util.HashMap;
import java.util.Map;

/**
 * @author WilsonSong
 * @date 2018/6/3
 * 不同的事件肯定是有不同的类型的
 */
public class EventModel {
    //例如，有人评论了一个问题，那type就是评论， actorId就是谁评论的，
    // entityId和entityType就是评论的是那个问题，entityOwnerId就是那个问题关联的对象
    private EventType type;    //事件的类型
    private int actorId;   //事件的触发者
    private int entityType;    //触发事件的载体
    private int entityId;  //和entityType组合成触发事件的载体  可以使任何一个实体的id，问题，评论，用户，站内信等等
    private int entityOwnerId;         //载体关联的对象

    public EventModel(){

    }

    public EventModel(EventType type){
        this.type = type;
    }

    //定义可扩展的字段
    private Map<String, String> exts = new HashMap<>();

    public EventModel setExts(String key, String value){
         exts.put(key,value);
         return this;
    }

    public String getExts(String key){
        return  exts.get(key);
    }

    public EventType getType() {
        return type;
    }

    //为了能够实现链状的设置
    public EventModel setType(EventType type) {
        this.type = type;
        return this;      //这个就是为了实现这个xxx.setType().setXX();
    }

    public int getActorId() {
        return actorId;
    }

    public EventModel setActorId(int actorId) {
        this.actorId = actorId;
        return this;
    }

    public int getEntityType() {
        return entityType;
    }

    public EventModel setEntityType(int entityType) {
        this.entityType = entityType;
        return this;
    }

    public int getEntityId() {
        return entityId;
    }

    public EventModel setEntityId(int entityId) {
        this.entityId = entityId;
        return this;
    }

    public int getEntityOwnerId() {
        return entityOwnerId;
    }

    public EventModel setEntityOwnerId(int entityOwnerId) {
        this.entityOwnerId = entityOwnerId;
        return this;
    }

    public Map<String, String> getExts() {
        return exts;
    }

    public EventModel setExts(Map<String, String> exts) {
        this.exts = exts;
        return this;
    }

}
