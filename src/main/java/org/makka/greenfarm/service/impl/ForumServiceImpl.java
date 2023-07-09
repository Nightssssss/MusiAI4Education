package org.makka.greenfarm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.makka.greenfarm.domain.Forum;
import org.makka.greenfarm.mapper.ForumMapper;
import org.makka.greenfarm.service.ForumService;
import org.makka.greenfarm.utils.UploadAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
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

    public boolean addForum(String uid, String title, String content,
                                MultipartFile image, HttpServletRequest request){
        //获取标题图的url
        String imageUrl = UploadAction.uploadForumImage(request,image) + "";
        Forum forum = new Forum();
        //设置帖子的各个属性
        //随机编号
        forum.setFid(String.valueOf(System.currentTimeMillis()));
        forum.setUid(uid);
        forum.setTitle(title);
        forum.setContent(content);
        forum.setPicture(imageUrl);
        forum.setPostTime(LocalDateTime.now());
        QueryWrapper<Forum> wrapper = new QueryWrapper<>();
        int result = forumMapper.insert(forum);
        //返回是否成功
        return result == 1;
    }
}
