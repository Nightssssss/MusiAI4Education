package org.makka.greenfarm.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.makka.greenfarm.domain.Owner;
import org.makka.greenfarm.mapper.OwnerMapper;
import org.makka.greenfarm.service.OwnerService;
import org.springframework.stereotype.Service;

@Service
public class OwnerServiceImpl extends ServiceImpl<OwnerMapper, Owner> implements OwnerService {
}
