package com.springboot.springboot.service;

import com.springboot.springboot.dao.FeedDAO;
import com.springboot.springboot.model.Feed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author WilsonSong
 * @date 2018/6/26
 * 新鲜事
 */
@Service
public class FeedService {
    @Autowired
    FeedDAO feedDAO;

    //拉取的模式
    public List<Feed> getUserFeeds (int max_id, List<Integer> user_ids, int count){
        return feedDAO.selectUserFeeds(max_id,user_ids,count);
    }

    public boolean addFeeds(Feed feed){
        feedDAO.addFeed(feed);
        return feed.getId()>0;
    }

    //推的模式,就是把所有的小时推送给你的粉丝，这里存储的就是id,由id这个核心数据来代表新鲜事
    //这样的话就不用存储新鲜事的数据，在推送的时候的压力就会减小
    public Feed getById(int id){
        return feedDAO.getFeedById(id);
    }
}
