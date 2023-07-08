package org.makka.greenfarm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.makka.greenfarm.domain.Progress;
import org.makka.greenfarm.mapper.ProgressMapper;
import org.makka.greenfarm.service.ProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProgressServiceImpl extends ServiceImpl<ProgressMapper, Progress> implements ProgressService {
    @Autowired
    private ProgressMapper progressMapper;

    public List<Progress> getProgressList(String uid) {
        QueryWrapper<Progress> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid", uid);
        return progressMapper.selectList(queryWrapper);
    }
}
