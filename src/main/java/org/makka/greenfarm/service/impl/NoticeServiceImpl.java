package org.makka.greenfarm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.makka.greenfarm.domain.Notice;
import org.makka.greenfarm.mapper.NoticeMapper;
import org.makka.greenfarm.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements NoticeService {
    @Autowired
    private NoticeMapper noticeMapper;

    @Cacheable(value = "notices", key = "3")
    public List<Notice> getNoticeList() {
        return noticeMapper.getNoticeList();
    }

    public Notice getNoticeByNid(String nid){
        return noticeMapper.getNoticeByNid(nid);
    }

    @Override
    public List<Notice> addNotice(Notice notice) {

        //生成公告编号

        //生成公告时间
        //



        noticeMapper.insert(notice);
        QueryWrapper<Notice> queryWrapper = new QueryWrapper<>();
        return noticeMapper.selectList(queryWrapper);
    }
}
