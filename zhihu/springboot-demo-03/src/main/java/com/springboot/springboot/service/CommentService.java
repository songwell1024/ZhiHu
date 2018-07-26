package com.springboot.springboot.service;

import com.springboot.springboot.dao.CommentDAO;
import com.springboot.springboot.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * @author WilsonSong
 * @date 2018/5/30
 */
@Service
public class CommentService{
    @Autowired
    CommentDAO commentDAO;

    @Autowired
    SensitiveService sensitiveService;

    //获取评论内容
    public List<Comment> selectCommentByEntity(int entity_id, int entity_type){
        return commentDAO.selectByEntity(entity_id,entity_type);
    }
    //获取评论数
    public int getCommentCount(int entity_id, int entity_type){
        return commentDAO.getCommentCount(entity_id,entity_type);
    }

    public int getUserCommentCount(int userId){
        return commentDAO.getUserCommentCount(userId);
    }

    //增加评论
    public int addComment(Comment comment){
        //过滤下
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        comment.setContent(sensitiveService.filter(comment.getContent()));
        return commentDAO.addComment(comment) > 0 ? comment.getId() : 0;
    }

    //更新,删除评论  其实就是把评论的状态置位1，而不是真正的删除

    public Boolean deleteComment(int entity_id, int entity_type){
        return commentDAO.updateStatus(entity_id,entity_type,1)>0;
    }

    //获取评论内容
    public Comment getCommentById(int id){
        return commentDAO.selectCommentById(id);
    }



}
