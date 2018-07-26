package com.springboot.springboot.async.handler;

import com.springboot.springboot.async.EventHandler;
import com.springboot.springboot.async.EventModel;
import com.springboot.springboot.async.EventType;
import com.springboot.springboot.service.SearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * 异步事件实现增加问题就直接加入solr的搜索库
 * @author WilsonSong
 * @date 2018/7/26/026
 */
@Component
public class AddQuestionHandler implements EventHandler{
    private static final Logger logger  = LoggerFactory.getLogger(AddQuestionHandler.class);

    @Autowired
    SearchService searchService;

    @Override
    public void doHander(EventModel model) {
        try{
            searchService.indexQuestion(model.getEntityId(),model.getExts("title"),model.getExts("content"));
        }catch (Exception e){
            logger.error("增加问题索引失败");
        }

    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.ADD_QUESTION);
    }
}
