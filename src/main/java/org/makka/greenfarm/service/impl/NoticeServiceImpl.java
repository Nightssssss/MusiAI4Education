package org.makka.greenfarm.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.makka.greenfarm.domain.Notice;
import org.makka.greenfarm.domain.ReserveProduct;
import org.makka.greenfarm.mapper.NoticeMapper;
import org.makka.greenfarm.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
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
        String nid = String.valueOf(System.currentTimeMillis());
        notice.setNid(nid);
        //生成公告时间
        notice.setPostTime(LocalDateTime.now());
        //插入图片,林煜煌冲冲冲!

        noticeMapper.insert(notice);
        QueryWrapper<Notice> queryWrapper = new QueryWrapper<>();
        return noticeMapper.selectList(queryWrapper);
    }

    @Override
    public List<Notice> updateNotice(Notice notice) {
        Notice notice1 = noticeMapper.getNoticeByNid(notice.getNid());

        notice1.setPostTime(notice.getPostTime());
        notice1.setTitle(notice.getTitle());
        notice1.setContent(notice.getContent());

        noticeMapper.updateById(notice1);

        QueryWrapper<Notice> queryWrapper = new QueryWrapper<>();
        return noticeMapper.selectList(queryWrapper);
    }

    @Override
    public List<Notice> deleteNoticeById(String nid) {

        QueryWrapper<Notice> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("nid",nid);
        noticeMapper.delete(queryWrapper);

        QueryWrapper<Notice> wrapper = new QueryWrapper<>();
        return noticeMapper.selectList(wrapper);
    }


}
