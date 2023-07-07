package org.makka.greenfarm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.makka.greenfarm.domain.ReserveProductComment;
import org.makka.greenfarm.domain.SaleProductComment;

import java.util.List;

public interface SaleProductCommentService extends IService<SaleProductComment> {
    //发表评论
    public List<SaleProductComment> PostSaleProductComment(String uid, String spid, String content);
}
