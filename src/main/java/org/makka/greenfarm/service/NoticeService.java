package org.makka.greenfarm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.makka.greenfarm.domain.Notice;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface NoticeService extends IService<Notice> {

    public List<Notice> getNoticeList();

    public Notice getNoticeByNid(String nid);

    public boolean updateNotice(String nid,String title, String content);
    public boolean deleteNoticeById(String nid);

    public boolean addNotice(String ownerid, String title, String content,MultipartFile image, HttpServletRequest request);

    public List<Notice> getNoticeListByOwnerId(String uid);

    public boolean updateNoticeImage(String noticeId,MultipartFile image, HttpServletRequest request);
}
