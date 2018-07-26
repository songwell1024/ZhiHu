package com.springboot.springboot.dao;

import com.springboot.springboot.model.Question;
import com.springboot.springboot.model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface questionDAO {

    //这个地方注意前后加空格，后面不注意的话可能就出错了
    String TABLE_NAME = " question ";
    String TABLE_FIELDS = " title, content, user_id, created_date, comment_count ";
    String SELECT_FIELDS = " id, " + TABLE_FIELDS;

    //增
    @Insert({"insert into ", TABLE_NAME,"(",TABLE_FIELDS,
            ") values(#{title}, #{content}, #{user_id}, #{created_date}, #{comment_count})"})
    int addQuestion(Question question);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " Where id = #{id} "})
     Question selectQuestionById(int id);

    //使用XML的方式完成数据库的操作
    List<Question> selectLatestQuestions(@Param("user_id") int userId, @Param("offset") int offset,
                                         @Param("limit") int limit);

    @Update({"update ", TABLE_NAME, "set comment_count = #{comment_count} where id  = #{id}"})
    int updateCommentCount(@Param("id") int id, @Param("comment_count") int comment_count);
}
