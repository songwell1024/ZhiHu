package com.springboot.springboot.controller;

import com.springboot.springboot.async.EventModel;
import com.springboot.springboot.async.EventProducer;
import com.springboot.springboot.async.EventType;
import com.springboot.springboot.model.Comment;
import com.springboot.springboot.model.EntityType;
import com.springboot.springboot.model.HostHolder;
import com.springboot.springboot.service.CommentService;
import com.springboot.springboot.service.questionService;
import com.springboot.springboot.utils.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

/**
 * @author WilsonSong
 * @date 2018/5/30
 */
@Controller
public class CommentController {
    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    @Autowired
    HostHolder hostHolder;

    @Autowired
    CommentService commentService;

    @Autowired
    questionService qService;

    @Autowired
    EventProducer eventProducer;

    @RequestMapping(path = {"/addComment"},method = RequestMethod.POST)
    public String addComment(@RequestParam("question_id") int question_id, @RequestParam("content") String content){
        try{
            Comment comment = new Comment();
            comment.setContent(content);
            if (hostHolder.getUser() != null){
                comment.setUser_id(hostHolder.getUser().getId());
            }else {
                //return  "redirect:/reglogin";
                comment.setUser_id(WendaUtil.ANONYMOUS_userId);
            }

            comment.setCreated_date(new Date());
            comment.setEntity_id(question_id);
            comment.setEntity_type(EntityType.ENTITY_QUESTION);  //这个实体就是question做的
            commentService.addComment(comment);

            int count = commentService.getCommentCount(comment.getEntity_id(),EntityType.ENTITY_QUESTION);
            qService.updateCommentCount(comment.getEntity_id(),count);    //这里的entry_id就是question的ID  从这里看comment.setEntity_id(question_id);

            eventProducer.fireEvent(new EventModel(EventType.COMMENT).setActorId(comment.getUser_id())
                    .setEntityId(question_id));

        }catch (Exception e){
            logger.error("增加评论失败" + e.getMessage());
        }
        return"redirect:/question/" +question_id;
    }
}
