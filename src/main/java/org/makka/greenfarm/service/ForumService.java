package org.makka.greenfarm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.makka.greenfarm.domain.Forum;

import java.util.List;

public interface ForumService extends IService<Forum> {
    public List<Forum> getForumList();

    public Forum getForumByFid(String fid);
}
