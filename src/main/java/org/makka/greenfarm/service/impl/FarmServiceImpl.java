package org.makka.greenfarm.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.makka.greenfarm.domain.Farm;
import org.makka.greenfarm.domain.FarmComment;
import org.makka.greenfarm.mapper.FarmMapper;
import org.makka.greenfarm.service.FarmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.xml.stream.events.Comment;
import java.lang.reflect.Field;
import java.util.List;

@Service
public class FarmServiceImpl extends ServiceImpl<FarmMapper, Farm> implements FarmService {
    @Autowired
    private FarmMapper farmMapper;

    //获取农场列表
    public List<Farm> getFarmList() {
        //获取所有农场信息
        List<Farm> farmList = farmMapper.selectList(null);
        return farmList;
    }

    //获取农场详细信息
    public Farm getFarmDetail(String farmId) {
        //获取农场详细信息
        Farm farm = farmMapper.selectById(farmId);
        farm.setCommentList(getFarmComment(farmId));
        return farm;
    }

    public List<FarmComment> getFarmComment(String farmId) {
        //级联查询评论信息，获取评论人的信息
        List<FarmComment> commentList = farmMapper.getFarmComment(farmId);
        return commentList;
    }
}
