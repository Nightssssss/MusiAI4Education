package org.makka.greenfarm.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.makka.greenfarm.domain.Farm;
import org.makka.greenfarm.mapper.FarmMapper;
import org.makka.greenfarm.service.FarmService;
import org.springframework.stereotype.Service;

@Service
public class FarmServiceImpl extends ServiceImpl<FarmMapper, Farm> implements FarmService {
}
