package com.springboot.springboot.async;

import com.alibaba.fastjson.JSONObject;
import com.springboot.springboot.utils.JedisAdapter;
import com.springboot.springboot.utils.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author WilsonSong
 * @date 2018/6/3
 * 事件的入口，用来统一分发事件，就是在队列中插入
 */
@Service
public class EventProducer {

    @Autowired
    JedisAdapter jedisAdapter;

    //把事件分发出去  EventProducer
    public boolean fireEvent(EventModel eventModel){
        try{

            //序列化，将EventModel 转换WieJSON的字符串
            String json = JSONObject.toJSONString(eventModel);
            String key = RedisKeyUtil.getEventQueueKey();
            jedisAdapter.lpush(key, json);
            return true;
        }catch (Exception e){
            return  false;
        }
    }

    //事件的取出与消费


}
