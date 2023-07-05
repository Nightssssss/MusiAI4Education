package org.makka.greenfarm.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.makka.greenfarm.domain.FarmComment;
import org.makka.greenfarm.mapper.FarmCommentMapper;
import org.makka.greenfarm.service.FarmCommentService;
import org.springframework.stereotype.Service;

@Service
public class FarmCommentServiceImpl extends ServiceImpl<FarmCommentMapper, FarmComment> implements FarmCommentService {
}
