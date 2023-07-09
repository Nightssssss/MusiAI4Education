package org.makka.greenfarm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.makka.greenfarm.domain.Forum;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ForumService extends IService<Forum> {
    public List<Forum> getForumList();

    public Forum getForumByFid(String fid);

    public boolean addForum(String uid, String title, String content,
                            MultipartFile image, HttpServletRequest request);
}
