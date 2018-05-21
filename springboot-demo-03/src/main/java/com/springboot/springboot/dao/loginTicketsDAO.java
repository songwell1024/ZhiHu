package com.springboot.springboot.dao;


import com.springboot.springboot.model.loginTickets;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.web.bind.annotation.RequestParam;

@Mapper
public interface loginTicketsDAO {

    String TABLE_NAME =  " login_tickets ";
    String TABLE_FIELDS = " user_id, ticket, expired, status ";
    String SELECT_FIELDS = " id " + TABLE_FIELDS;

    @Insert({"insert into", TABLE_NAME, "(",TABLE_FIELDS, ") value(#{user_id}, #{ticket}, #{expired}, #{status})"})
    int addTicket(loginTickets loginTicket);

    @Select({"select", TABLE_FIELDS," from", TABLE_NAME, " where ticket = #{ticket}"})
    loginTickets selectTicket(String ticket);

    @Update({"update", TABLE_NAME, " set status = #{status} where ticket = #{ticket}"})
    void updateStatus(@RequestParam("ticket") String ticket, @RequestParam("status") int status);

}
