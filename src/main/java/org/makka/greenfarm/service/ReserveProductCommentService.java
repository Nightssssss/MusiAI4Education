package org.makka.greenfarm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.makka.greenfarm.domain.FarmComment;
import org.makka.greenfarm.domain.ReserveProductComment;

import java.util.List;

public interface ReserveProductCommentService extends IService<ReserveProductComment> {
    public List<ReserveProductComment> PostReserveProductComment(String uid, String rpid, String content);
}
