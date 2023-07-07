package org.makka.greenfarm.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.makka.greenfarm.common.CommonResponse;
import org.makka.greenfarm.domain.FarmComment;
import org.makka.greenfarm.mapper.FarmCommentMapper;
import org.makka.greenfarm.service.FarmCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class FarmCommentServiceImpl extends ServiceImpl<FarmCommentMapper, FarmComment> implements FarmCommentService {
    @Autowired
    private FarmCommentMapper farmCommentMapper;

    //发布评论
    public List<FarmComment> PostFarmComment(String uid,String farmId,String content) {
        //插入评论
        FarmComment farmComment = new FarmComment();
        //插入timestamp类型的当期时间
        farmComment.setCommentTime(LocalDateTime.now());
        String cid = String.valueOf(System.currentTimeMillis());
        farmComment.setCid(cid);
        farmComment.setUid(uid);
        farmComment.setFid(farmId);
        farmComment.setContent(content);
        farmCommentMapper.insert(farmComment);
        //获取所有评论
        List<FarmComment> commentList = farmCommentMapper.getFarmCommentList(farmId);
        return commentList;
    }
}
