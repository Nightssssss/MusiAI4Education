package org.makka.greenfarm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.makka.greenfarm.domain.Progress;

import java.util.List;

public interface ProgressService extends IService<Progress> {
    public List<Progress> getProgressList(String uid);

    public Progress getProgressByPid(String pid);
}
