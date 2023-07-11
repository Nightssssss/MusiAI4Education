package org.makka.greenfarm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.makka.greenfarm.domain.ForumComment;

import java.util.List;

public interface ForumCommentService extends IService<ForumComment> {
    public List<ForumComment> addForumComment(String uid, String forumId, String content);
}
