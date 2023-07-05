package org.makka.greenfarm.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.makka.greenfarm.domain.ForumComment;
import org.makka.greenfarm.mapper.ForumCommentMapper;
import org.makka.greenfarm.service.ForumCommentService;
import org.springframework.stereotype.Service;

@Service
public class ForumCommentServiceImpl extends ServiceImpl<ForumCommentMapper, ForumComment> implements ForumCommentService {

}
