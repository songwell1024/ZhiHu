package com.springboot.springboot.model;

import java.util.Date;

/**
 * @author WilsonSong
 * @date 2018/5/31
 * 站内信
 */
public class Message {
    private int id;
    private int from_id;
    private int to_id;
    private String content;
    private Date created_date;
    private int has_read;
    private String conversation_id;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFrom_id() {
        return from_id;
    }

    public void setFrom_id(int from_id) {
        this.from_id = from_id;
    }

    public int getTo_id() {
        return to_id;
    }

    public void setTo_id(int to_id) {
        this.to_id = to_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreated_date() {
        return created_date;
    }

    public void setCreated_date(Date created_date) {
        this.created_date = created_date;
    }

    public int getHas_read() {
        return has_read;
    }

    public void setHas_read(int has_read) {
        this.has_read = has_read;
    }

    //站内消息的id保持一致
    public String getConversationId() {
        if(from_id < to_id){
            conversation_id = String.format("%d_%d",from_id, to_id);
            return conversation_id;
        }
        else{
            conversation_id =String.format("%d_%d",to_id,from_id);
            return conversation_id;
        }
    }
    public void setConversationId(String conversation_id) {
        this.conversation_id = conversation_id;
    }

}
