package org.makka.greenfarm.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.makka.greenfarm.domain.FarmComment;
import org.makka.greenfarm.domain.ReserveProduct;
import org.makka.greenfarm.domain.ReserveProductComment;
import org.makka.greenfarm.mapper.FarmCommentMapper;
import org.makka.greenfarm.mapper.ReserveProductCommentMapper;
import org.makka.greenfarm.service.ReserveProductCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReserveProductCommentServiceImpl extends ServiceImpl<ReserveProductCommentMapper, ReserveProductComment> implements ReserveProductCommentService {
    @Autowired
    private ReserveProductCommentMapper reserveProductCommentMapper;
    @Override
    public List<ReserveProductComment> PostReserveProductComment(String uid, String rpid, String content) {
        //插入评论
        ReserveProductComment reserveProductComment = new ReserveProductComment();
        //插入timestamp类型的当期时间
        reserveProductComment.setCommentTime(LocalDateTime.now());
        String cid = String.valueOf(System.currentTimeMillis());
        reserveProductComment.setCid(cid);
        reserveProductComment.setUid(uid);
        reserveProductComment.setRpid(rpid);
        reserveProductComment.setContent(content);
        baseMapper.insert(reserveProductComment);
        //获取所有评论
        List<ReserveProductComment> commentList = reserveProductCommentMapper.getReserveCommentList(rpid);
        return commentList;
    }
}
