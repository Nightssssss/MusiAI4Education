package org.makka.greenfarm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.makka.greenfarm.domain.FarmComment;
import org.makka.greenfarm.domain.ForumComment;
import org.makka.greenfarm.mapper.ForumCommentMapper;
import org.makka.greenfarm.service.ForumCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ForumCommentServiceImpl extends ServiceImpl<ForumCommentMapper, ForumComment> implements ForumCommentService {
    @Autowired
    private ForumCommentMapper forumCommentMapper;

    public List<ForumComment> addForumComment(String uid, String forumId, String content){
        //插入评论
        ForumComment forumComment = new ForumComment();
        //插入timestamp类型的当期时间
        forumComment.setCommentTime(LocalDateTime.now());
        String cid = String.valueOf(System.currentTimeMillis());
        forumComment.setCid(cid);
        forumComment.setUid(uid);
        forumComment.setFid(forumId);
        forumComment.setContent(content);
        forumCommentMapper.insert(forumComment);
        //获取所有评论
        List<ForumComment> commentList = forumCommentMapper.getForumCommentList(forumId);
        return commentList;
    }
}
