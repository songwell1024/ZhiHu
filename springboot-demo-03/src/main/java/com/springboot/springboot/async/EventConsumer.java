package com.springboot.springboot.async;

import com.alibaba.fastjson.JSON;
import com.springboot.springboot.utils.JedisAdapter;
import com.springboot.springboot.utils.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author WilsonSong
 * @date 2018/6/3
 * 处理队列中的事件并与各个handler沟通
 * InitializingBean接口的作用在spring 初始化后，执行完所有属性设置方法(即setXxx)将
 * 自动调用 afterPropertiesSet(), 在配置文件中无须特别的配置
 */
@Service
public class EventConsumer  implements InitializingBean,ApplicationContextAware{
    private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);
    private Map<EventType, List<EventHandler>> config = new HashMap<>();
    private ApplicationContext applicationContext;

    @Autowired
    JedisAdapter jedisAdapter;

    //这个方法将在所有的属性被初始化后调用
    @Override
    public void afterPropertiesSet() throws Exception {
        //获取现在有多少个eventHandler初始化了
        Map<String, EventHandler> beans = applicationContext.getBeansOfType(EventHandler.class);
        if (beans != null){
            for (Map.Entry<String,EventHandler> entry : beans.entrySet()){
                List<EventType> eventTypes = entry.getValue().getSupportEventTypes();

                for (EventType type : eventTypes){
                    if (!config.containsKey(type)){
                        //把handler放到config中
                        config.put(type, new ArrayList<EventHandler>());
                    }
                    config.get(type).add(entry.getValue());
                }
            }
        }

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    String key  = RedisKeyUtil.getEventQueueKey();
                    List<String> events = jedisAdapter.brpop(0,key);
                    for (String message : events){
                        if (message.equals(key)){
                            continue;
                        }
                        EventModel eventModel = JSON.parseObject(message,EventModel.class);
                        if (config.containsKey(eventModel.getType())){
                            logger.error("不能识别的事件");
                            continue;
                        }

                        for (EventHandler handler : config.get(eventModel.getType())){
                            handler.doHander(eventModel);
                        }
                    }
                }
            }
        });
        thread.start();

    }

    //将config中所有的配置的接口
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
