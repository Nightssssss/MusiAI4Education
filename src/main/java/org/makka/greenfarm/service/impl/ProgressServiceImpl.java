package org.makka.greenfarm.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.makka.greenfarm.domain.Progress;
import org.makka.greenfarm.mapper.ProgressMapper;
import org.makka.greenfarm.service.ProgressService;
import org.springframework.stereotype.Service;

@Service
public class ProgressServiceImpl extends ServiceImpl<ProgressMapper, Progress> implements ProgressService {
}
