package com.springboot.springboot.controller;

import com.springboot.springboot.async.EventModel;
import com.springboot.springboot.async.EventProducer;
import com.springboot.springboot.async.EventType;
import com.springboot.springboot.model.Comment;
import com.springboot.springboot.model.EntityType;
import com.springboot.springboot.model.HostHolder;
import com.springboot.springboot.service.CommentService;
import com.springboot.springboot.service.LikeService;
import com.springboot.springboot.utils.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author WilsonSong
 * @date 2018/6/2
 * 点赞点踩功能的实现
 */
@Controller
public class LikeController {
    private static final Logger logger  = LoggerFactory.getLogger(LikeController.class);

    @Autowired
    LikeService likeService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    CommentService commentService;

    @Autowired
    EventProducer eventProducer;

    //赞
    @RequestMapping(path = {"/like"}, method = RequestMethod.POST)
    @ResponseBody
    public String  like(@RequestParam("commentId") int comment_id){
        if(hostHolder.getUser() == null){
            return "redirect:/reglogin";
        }

        Comment comment = commentService.getCommentById(comment_id);

        eventProducer.fireEvent(new EventModel(EventType.LIKE)      //把这个event发出去
                .setActorId(hostHolder.getUser().getId()).setEntityId(comment_id) //点赞的内容就是评论的id
                .setEntityType(EntityType.ENTITY_COMMENT).setEntityOwnerId(comment.getUser_id())
                .setExts("questionId", String.valueOf(comment.getEntity_id())));

        long likeCount = likeService.like(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT, comment_id);
        return WendaUtil.getJsonString(0,String.valueOf(likeCount));

    }

    //踩
    @RequestMapping(path = {"/dislike"}, method = RequestMethod.POST)
    @ResponseBody
    public String  dislike(@RequestParam("commentId") int comment_id){
        if(hostHolder.getUser() == null){
            return "redirect:/reglogin";
        }

        long likeCount = likeService.dislike(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT, comment_id);
        return WendaUtil.getJsonString(0,String.valueOf(likeCount));

    }

}
