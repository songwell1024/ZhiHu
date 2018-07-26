package com.springboot.springboot.async;

import java.util.List;

/**
 * @author WilsonSong
 * @date 2018/6/3
 * 用来处理事件的，谁关心这个事件，谁来做这个事件
 */
public interface EventHandler {

    void doHander(EventModel model); //谁来处理事件

    List<EventType> getSupportEventTypes();  //有哪些关心这些事件的

}
