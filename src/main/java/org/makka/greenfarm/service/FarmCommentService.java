package org.makka.greenfarm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.makka.greenfarm.domain.FarmComment;

import java.util.List;

public interface FarmCommentService extends IService<FarmComment> {
    public List<FarmComment> PostFarmComment(String uid,String farmId,String content);
}
