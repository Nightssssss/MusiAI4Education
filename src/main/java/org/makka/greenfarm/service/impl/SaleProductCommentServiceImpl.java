package org.makka.greenfarm.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.makka.greenfarm.domain.SaleProductComment;
import org.makka.greenfarm.mapper.SaleProductCommentMapper;
import org.makka.greenfarm.service.SaleProductCommentService;
import org.springframework.stereotype.Service;

@Service
public class SaleProductCommentServiceImpl extends ServiceImpl<SaleProductCommentMapper, SaleProductComment> implements SaleProductCommentService {
}
