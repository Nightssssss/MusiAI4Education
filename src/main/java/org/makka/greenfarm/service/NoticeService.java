package org.makka.greenfarm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.makka.greenfarm.domain.Notice;

import java.util.List;

public interface NoticeService extends IService<Notice> {

    public List<Notice> getNoticeList();

    public Notice getNoticeByNid(String nid);

    public List<Notice> addNotice(Notice notice);
}
