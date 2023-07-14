package org.makka.greenfarm.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.makka.greenfarm.domain.Farm;
import org.makka.greenfarm.domain.Forum;
import org.makka.greenfarm.domain.Notice;
import org.makka.greenfarm.domain.ReserveProduct;
import org.makka.greenfarm.mapper.FarmMapper;
import org.makka.greenfarm.mapper.NoticeMapper;
import org.makka.greenfarm.service.NoticeService;
import org.makka.greenfarm.utils.UploadAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements NoticeService {
    @Autowired
    private NoticeMapper noticeMapper;

    @Autowired
    private FarmMapper farmMapper;

    public List<Notice> getNoticeList() {
        return noticeMapper.getNoticeList();
    }

    public Notice getNoticeByNid(String nid){
        return noticeMapper.getNoticeByNid(nid);
    }

    @Override
    public boolean addNotice(String ownerid, String title, String content,
                            MultipartFile image, HttpServletRequest request){
        QueryWrapper<Farm> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("ownerid",ownerid);
        Farm farm = farmMapper.selectOne(wrapper1);
        String fid = farm.getFid();
        //获取标题图的url
        String imageUrl = UploadAction.uploadNoticeImage(request,image);
        Notice notice = new Notice();
        //设置帖子的各个属性
        //随机编号
        notice.setNid(String.valueOf(System.currentTimeMillis()));
        notice.setTitle(title);
        notice.setContent(content);
        notice.setFid(fid);
        notice.setPicture(imageUrl);
        notice.setPostTime(LocalDateTime.now());
        //插入数据库
        QueryWrapper<Forum> wrapper = new QueryWrapper<>();
        int result = noticeMapper.insert(notice);
        //返回是否成功
        return result == 1;
    }

    @Override
    public boolean updateNotice(String nid,String title, String content) {
        Notice notice = noticeMapper.getNoticeByNid(nid);

        notice.setPostTime(LocalDateTime.now());
        notice.setTitle(title);
        notice.setContent(content);

        int result = noticeMapper.updateById(notice);

        return result == 1;
    }

    @Override
    public boolean updateNoticeImage(String noticeId,MultipartFile image, HttpServletRequest request) {
        String imageUrl = UploadAction.uploadNoticeImage(request,image);
        Notice notice = noticeMapper.getNoticeByNid(noticeId);
        System.out.println(imageUrl);
        notice.setPicture(imageUrl);
        int result = noticeMapper.updateById(notice);
        return result == 1;
    }

    @Override
    public boolean deleteNoticeById(String nid) {

        QueryWrapper<Notice> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("nid",nid);
        int result = noticeMapper.delete(queryWrapper);

        return result == 1;
    }

    @Override
    public List<Notice> getNoticeListByOwnerId(String ownerid) {
        QueryWrapper<Farm> wrapper = new QueryWrapper<>();
        wrapper.eq("ownerid",ownerid);
        Farm farm = farmMapper.selectOne(wrapper);
        String fid = farm.getFid();
        QueryWrapper<Notice> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("fid",fid);
        return noticeMapper.selectList(wrapper1);
    }

}
