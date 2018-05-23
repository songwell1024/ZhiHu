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
            ") Values(#{title}, #{content}, #{user_id}, #{created_date}, #{comment_count})"})
    int addQuestion(Question question);

    //使用XML的方式完成数据库的操作
    List<Question> selectLatestQuestions(@Param("user_id") int userId, @Param("offset") int offset,
                                         @Param("limit") int limit);


}
