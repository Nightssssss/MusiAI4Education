package org.makka.greenfarm.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.makka.greenfarm.domain.ReserveProductComment;
import org.makka.greenfarm.domain.SaleProductComment;
import org.makka.greenfarm.mapper.ReserveProductCommentMapper;
import org.makka.greenfarm.mapper.SaleProductCommentMapper;
import org.makka.greenfarm.service.SaleProductCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SaleProductCommentServiceImpl extends ServiceImpl<SaleProductCommentMapper, SaleProductComment> implements SaleProductCommentService {

    @Autowired
    private SaleProductCommentMapper saleProductCommentMapper;
    @Override
    public List<SaleProductComment> PostSaleProductComment(String uid, String spid, String content) {
        //插入评论
        SaleProductComment saleProductComment = new SaleProductComment();
        //插入timestamp类型的当期时间
        saleProductComment.setCommentTime(LocalDateTime.now());
        String cid = String.valueOf(System.currentTimeMillis());
        saleProductComment.setCid(cid);
        saleProductComment.setUid(uid);
        saleProductComment.setSpid(spid);
        saleProductComment.setContent(content);
        baseMapper.insert(saleProductComment);
        //获取所有评论
        List<SaleProductComment> commentList = saleProductCommentMapper.getSaleCommentList(spid);
        return commentList;
    }
}
