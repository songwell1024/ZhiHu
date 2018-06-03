package com.springboot.springboot.service;

import com.springboot.springboot.utils.JedisAdapter;
import com.springboot.springboot.utils.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author WilsonSong
 * @date 2018/6/2
 * 踩赞功能
 */
@Service
public class LikeService {

    @Autowired
    JedisAdapter jedisAdapter;

    //显示当前有多少人喜欢
    public long getLikeCount(int entityType, int entityId ){
        String likeKey = RedisKeyUtil.getLikeKey(entityType,entityId);
        return jedisAdapter.scard(likeKey);
    }

    //显示某个用户对某个内容是点赞还是点踩
    public int getLikeStatus(int userID, int entityType, int entityId){
        //点了赞
        String likeKey = RedisKeyUtil.getLikeKey(entityType,entityId);
        if (jedisAdapter.sismember(likeKey,String.valueOf(userID))){
            return 1;
        }

        //-1的话点了踩， 0 的话什么都不是
        String dislikeKey = RedisKeyUtil.getDislikeKey(entityType,entityId);
        return jedisAdapter.sismember(dislikeKey,String.valueOf(userID))? -1 : 0;
    }

    //点赞
    public long like(int userID, int entityType, int entityId){
        String likeKey = RedisKeyUtil.getLikeKey(entityType,entityId);
        jedisAdapter.sadd(likeKey,String.valueOf(userID));

        //因为一个人点完赞不可能再点踩，所以把点踩去掉
        String dislikeKey = RedisKeyUtil.getDislikeKey(entityType,entityId);
        jedisAdapter.srem(dislikeKey,String.valueOf(userID));

        return jedisAdapter.scard(likeKey);
    }

    //点踩
    public long dislike(int userID, int entityType, int entityId){
        String dislikeKey = RedisKeyUtil.getDislikeKey(entityType,entityId);
        jedisAdapter.sadd(dislikeKey,String.valueOf(userID));

        //因为一个人点完赞不可能再点踩，所以把点踩去掉
        String likeKey = RedisKeyUtil.getLikeKey(entityType,entityId);
        jedisAdapter.srem(likeKey,String.valueOf(userID));

        return jedisAdapter.scard(likeKey);
    }



}
