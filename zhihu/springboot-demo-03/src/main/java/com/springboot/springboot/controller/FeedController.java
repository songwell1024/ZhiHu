package com.springboot.springboot.controller;

import com.springboot.springboot.model.EntityType;
import com.springboot.springboot.model.Feed;
import com.springboot.springboot.model.HostHolder;
import com.springboot.springboot.service.FeedService;
import com.springboot.springboot.service.FollowService;
import com.springboot.springboot.utils.JedisAdapter;
import com.springboot.springboot.utils.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * @author WilsonSong
 * @date 2018/6/26
 * 推拉模式显示新鲜事
 */
@Controller
public class FeedController {
    @Autowired
    FeedService feedService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    FollowService followService;
    @Autowired
    JedisAdapter jedisAdapter;

    //拉取的模式
    //找到我关注的人
    @RequestMapping(path = {"/pullfeeds"}, method = RequestMethod.GET)
    private String getPullFeeds(Model model){
        int localUserId = hostHolder.getUser() == null ? 0 : hostHolder.getUser().getId();
        List<Integer> followees = new ArrayList<>();
        if (localUserId != 0){
            followees = followService.getFollowees(localUserId, EntityType.ENTITY_USER, Integer.MAX_VALUE);
        }
        List<Feed> feeds = feedService.getUserFeeds(Integer.MAX_VALUE,followees,10);
        model.addAttribute("feeds",feeds);
        return "feeds";
    }

    //推送的模式
    @RequestMapping(path = {"/pushfeeds"}, method = {RequestMethod.GET, RequestMethod.POST})
    private String getPushFeeds(Model model){
        int localUserId = hostHolder.getUser()==null ? 0: hostHolder.getUser().getId();
        //从这个redis中把ID取出来就可以了 之前在handler中已经推进去了然后现在取出来就可以了
        List<String> feedsId = jedisAdapter.lrange(RedisKeyUtil.getTimelineKey(localUserId),0, 10);

        List<Feed> feeds = new ArrayList<>();
        for (String feedId : feedsId){
          Feed feed =  feedService.getById(Integer.parseInt(feedId));       //
          if(feed == null){
              continue;
          }
          feeds.add(feed);
        }
        model.addAttribute("feeds", feeds);
        return "feeds";
    }
}
