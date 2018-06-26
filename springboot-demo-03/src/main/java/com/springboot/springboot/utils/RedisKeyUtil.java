package com.springboot.springboot.utils;

/**
 * @author WilsonSong
 * @date 2018/6/2
 * 为了防止生成的key有冲突
 */
public class RedisKeyUtil {
    private static String SPLIT = ":";
    private static String BIZ_LIKE = "LIKE";
    private static String BIZ_DISLIKE = "DISLIKE";
    private static String BIZ_EVENTQUEUE = "EVENTQUEUE";

    //粉丝
    private static String BIZ_FOLLOWER = "FOLLOWER";

    //关注对象
    private static String BIZ_FOLLOWEE = "FOLLOWEE";

    //timeline推模式的key
    private static String BIZ_TIMELINE = "TIMELINE";

    //获取点赞的key
    public static String getLikeKey(int entityType, int entityId){
        return BIZ_LIKE + SPLIT + String.valueOf(entityType) + SPLIT +String.valueOf(entityId);
    }

    //获取点踩的key
    public static String getDislikeKey(int entityType, int entityId){
        return BIZ_DISLIKE +SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }

    public static  String  getEventQueueKey(){
        return BIZ_EVENTQUEUE;
    }

    //获取每一个实体（问题或者是人）的粉丝对象的key
    public static String getFollowerKey(int entityId, int entityType){
        return BIZ_FOLLOWER + SPLIT + String.valueOf(entityId) + SPLIT + String.valueOf(entityType);
    }

    //获取关注的实体的key也就是某个人关注的某一类问题
    public static String getFolloweeKey(int userId, int entityType){
        return BIZ_FOLLOWEE + SPLIT + String.valueOf(userId) + SPLIT + String.valueOf(entityType);
    }

    //获取TimeLine的key
    public static String getTimelineKey(int userId){
        return BIZ_TIMELINE + SPLIT + String.valueOf(userId);
    }
}
