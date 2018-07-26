package com.springboot.springboot.async.handler;


import com.alibaba.fastjson.JSONObject;
import com.springboot.springboot.async.EventHandler;
import com.springboot.springboot.async.EventModel;
import com.springboot.springboot.async.EventType;
import com.springboot.springboot.model.*;
import com.springboot.springboot.service.*;
import com.springboot.springboot.utils.JedisAdapter;
import com.springboot.springboot.utils.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author WilsonSong
 * @date 2018/6/24
 * 新鲜事引发的事件
 */
@Component
public class FeedHandler implements EventHandler {

    @Autowired
    MessageService messageService;
    @Autowired
    userService uService;
    @Autowired
    questionService qService;
    @Autowired
    FeedService feedService;
    @Autowired
    FollowService followService;
    @Autowired
    JedisAdapter jedisAdapter;

    //feed的data是与很多内容的，然后就需要取出来然后整合成一个JSON串的格式
    public String buildFeedData(EventModel model){
        Map<String, String> map = new HashMap<>();
        User user = uService.getUser(model.getActorId());
        if (user ==null){
            return null;
        }
        map.put("user_id", String.valueOf(user.getId()));
        map.put("user_name", user.getName());
        map.put("head_url", user.getHead_url());

        if (model.getType() ==  EventType.COMMENT || model.getType() ==  EventType.FOLLOW
                && model.getEntityType() == EntityType.ENTITY_QUESTION){          //针对于问题如果发生了评论或者是关注
            Question question = qService.selectQuestionById(model.getEntityId());
            if (question == null){
                return null;
            }
            map.put("question_id", String.valueOf(question.getId()));
            map.put("question_title", question.getTitle());
            return JSONObject.toJSONString(map);
        }
        return  null;
    }

    //系统发送通知的事件
    @Override
    public void doHander(EventModel model) {

        //方便测试把ActorId设置为随机值
//        Random r = new Random();
//        model.setActorId(1+ r.nextInt(10));     //随机生成1-10之间的值


        Feed feed = new Feed();
        feed.setCreated_date(new Date());
        feed.setUser_id(model.getActorId());    //谁触发的
        feed.setType(model.getType().getValue());
        feed.setData(buildFeedData(model));
        if (feed.getData() == null){      //即使feed流是空的话就不处理
            return;
        }
        feedService.addFeeds(feed);     //把feed 流加进来

        //给事件的粉丝推送
        List<Integer> followers = followService.getFollowers(model.getActorId(),EntityType.ENTITY_USER, Integer.MAX_VALUE);
        followers.add(0);         //未登录的时候显示系统的东西
        for (int follower: followers){
            String timelineKey = RedisKeyUtil.getTimelineKey(follower);
            jedisAdapter.lpush(timelineKey,String.valueOf(feed.getId()));          //把timeline的事件放到队列中
        }


    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(new EventType[]{EventType.COMMENT, EventType.FOLLOW});               //支持的事件就是comment和follow事件就是说处理事件的队列中有这两个事件
    }
}
