package com.springboot.springboot.dao;


import com.springboot.springboot.model.loginTickets;
import org.apache.ibatis.annotations.*;

@Mapper
public interface loginTicketsDAO {

    String TABLE_NAME =  " login_ticket ";
    String TABLE_FIELDS = " user_id, ticket, expired, status ";
    String SELECT_FIELDS = " id, " + TABLE_FIELDS;

    @Insert({"insert into", TABLE_NAME, "(",TABLE_FIELDS, ") values (#{user_id}, #{ticket}, #{expired}, #{status})"})
    int addTicket(loginTickets ticket);

    @Select({"select", SELECT_FIELDS," from", TABLE_NAME, " where ticket = #{ticket}"})
    loginTickets selectByTicket(String ticket);

    @Update({"update", TABLE_NAME, " set status = #{status} where ticket = #{ticket}"})
    void updateStatus(@Param("ticket") String ticket, @Param("status") int status);

}
