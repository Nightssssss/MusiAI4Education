package org.makka.greenfarm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.makka.greenfarm.domain.Notice;
import org.makka.greenfarm.mapper.NoticeMapper;
import org.makka.greenfarm.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements NoticeService {
    @Autowired
    private NoticeMapper noticeMapper;

    public List<Notice> getNoticeList() {
        QueryWrapper<Notice> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("postTime");
        return noticeMapper.selectList(queryWrapper);
    }

    public Notice getNoticeByNid(String nid){
        QueryWrapper<Notice> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("nid",nid);
        return noticeMapper.selectOne(queryWrapper);
    }
}
