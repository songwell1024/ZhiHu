package com.springboot.springboot.dao;

import com.springboot.springboot.model.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface userDAO {

    //这个地方注意前后加空格，后面不注意的话可能就出错了
    String TABLE_NAME = " user ";
    String TABLE_FIELDS = " name , password , salt , head_url ";
    String SELECT_FIELDS = " id, " + TABLE_FIELDS;

    //增
    @Insert({"insert into ", TABLE_NAME,"(",TABLE_FIELDS,
            ") values(#{name}, #{password}, #{salt}, #{head_url})"})
    int addUser(User user);
    //查
    @Select({"select ",SELECT_FIELDS,"from",TABLE_NAME, "where id=#{id}"})
    User selectById(int id);


    @Select({"select ",SELECT_FIELDS,"from",TABLE_NAME, "where name=#{name}"})
    User selectByName(String name );

    //改
    @Update({"update",TABLE_NAME,"set password = #{password} where id = #{id}"})
    void updatePassword(User user);

    //删
    @Delete({"delete from",TABLE_NAME,"where id = #{id}"})
    void deleteUserById(int id);
}
