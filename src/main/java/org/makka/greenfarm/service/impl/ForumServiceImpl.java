package org.makka.greenfarm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.makka.greenfarm.domain.Forum;
import org.makka.greenfarm.mapper.ForumMapper;
import org.makka.greenfarm.service.ForumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ForumServiceImpl extends ServiceImpl<ForumMapper, Forum> implements ForumService {
    @Autowired
    private ForumMapper forumMapper;

    public List<Forum> getForumList() {
        return forumMapper.getForumList();
    }

    public Forum getForumByFid(String fid){
        Forum forum = forumMapper.getForum(fid);
        forum.setCommentList(forumMapper.getForumComment(fid));
        return forum;
    }
}
