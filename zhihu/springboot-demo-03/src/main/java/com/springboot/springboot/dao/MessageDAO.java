package com.springboot.springboot.dao;

import com.springboot.springboot.model.Message;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author WilsonSong
 * @date 2018/5/31/031
 */
@Mapper
public interface MessageDAO {

     String TABLE_NAME = " message ";
     String TABLE_FIELDS = " from_id, to_id, content, created_date, has_read, conversation_id ";
     String SELECT_FIELDS = " id, " + TABLE_FIELDS;

     //插入消息
     @Insert({"insert into ", TABLE_NAME, "(", TABLE_FIELDS, ") values (#{from_id}, " +
             "#{to_id}, #{content}, #{created_date}, #{has_read}, #{conversation_id})"})
     int addMessage(Message message);

     //消息详情  分页显示
     @Select({"select ", SELECT_FIELDS, "from ", TABLE_NAME, "where conversation_id = #{conversation_id} " +
             " order by id desc limit #{offset}, #{limit}"})
     List<Message> getConversationDetail(@Param("conversation_id") String conversation_id, @Param("offset") int offset, @Param("limit") int limit);

     //获取未读消息
     @Select({"select count(id) from ", TABLE_NAME, " where has_read = 0 and to_id = #{user_id} and conversation_id = #{conversation_id}"})
     int getUnreadConversationCount(@Param("user_id") int user_id, @Param("conversation_id") String Conversation_id);

     //获取消息列表
     @Select({"select ", TABLE_FIELDS, " ,count(id) as id from ( select * from ", TABLE_NAME,
             " where from_id = #{user_id} or to_id = #{user_id} order by id desc) tt group by conversation_id  order by created_date desc limit #{offset}, #{limit}"})
     List<Message> getConversationList(@Param("user_id") int user_id, @Param("offset") int offset, @Param("limit") int limit );

     //当消息已读的时候更新为已读
     @Update({"update ", TABLE_NAME," set has_read = #{has_read} where conversation_id = #{conversation_id}"})
     int updateConversationReadStatus(@Param("conversation_id") String conversation_id,@Param("has_read") int has_read);


    }
