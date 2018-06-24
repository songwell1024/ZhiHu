package com.springboot.springboot.dao;

import com.springboot.springboot.model.Comment;
import org.apache.ibatis.annotations.*;

import javax.validation.constraints.Max;
import java.util.List;

/**
 * @author WilsonSong
 * @date 2018/5/30
 *  对接数据库的comment表
 */
@Mapper
public interface CommentDAO {

    String TABLE_NAME = " comment ";
    String INSERT_FIELDS = " user_id, content, entity_id, entity_type, created_date, status ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    //增加内容
    @Insert({"insert into ", TABLE_NAME, "(",INSERT_FIELDS, ") values (#{user_id}, #{content}, " +
            "#{entity_id}, #{entity_type}, #{created_date}, #{status})" })
    int addComment(Comment comment);

    //更新
    @Update({"update ",TABLE_NAME, " set status = #{status} where entity_id = #{entity_id} and entity_type = #{entity_type}"})
    int updateStatus(@Param("entity_id") int entity_id, @Param("entity_type") int entity_type,@Param("status") int status);

    //通过ID查询内容
    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, "where id  = #{id}"})
    Comment selectCommentById(int id);   //这个只有一条，就不用list了


    //选出内容
    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME," where entity_id = #{entity_id} and entity_type = #{entity_type} order by created_date desc"})
    List<Comment> selectByEntity(@Param("entity_id") int entity_id, @Param("entity_type") int entity_type);

    @Select({"select count(id) from ", TABLE_NAME," where entity_id = #{entity_id} and entity_type = #{entity_type}"})
    int getCommentCount(@Param("entity_id") int entity_id, @Param("entity_type") int entity_type );

    @Select({"select count(id) from", TABLE_NAME, " where user_id = #{user_id}"})
    int getUserCommentCount(@Param("user_id") int user_id);

}
