package com.springboot.springboot.dao;

import com.springboot.springboot.model.Feed;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author WilsonSong
 * @date 2018/6/26
 * 新鲜事的DAO层
 */
@Mapper
public interface FeedDAO {
     String TABLE_NAME = " feed ";
     String TABLE_FIELDS = " created_date, user_id, data, type ";
     String SELECT_FIELDS = " id, " + TABLE_FIELDS;

     @Insert({"insert into ", TABLE_NAME , "( ", TABLE_FIELDS ,") values (#{created_date}, #{user_id}, #{data}, #{type})"})
     int addFeed(Feed feed);

    //推的模式，就是把新鲜事从数据库中取出来然后推给用户
     @Select({"select ",SELECT_FIELDS , " from ", TABLE_NAME, " where id = #{id}"})
     Feed getFeedById(int id);

    //max_id是增量的拉取
    //没登录的时候看到的是所有新鲜事，这个时候user_id是没有用的
    //当登陆的时候就要用到user_id然后看自己关注的事情
    //动态的SQL
     List<Feed> selectUserFeeds(@Param("max_id") int max_id, @Param("user_ids") List<Integer> user_ids, @Param("count") int count);
}
