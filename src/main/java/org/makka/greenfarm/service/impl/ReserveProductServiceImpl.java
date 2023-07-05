package org.makka.greenfarm.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.makka.greenfarm.domain.ReserveProduct;
import org.makka.greenfarm.mapper.ReserveProductMapper;
import org.makka.greenfarm.service.ReserveProductService;
import org.springframework.stereotype.Service;

@Service
public class ReserveProductServiceImpl extends ServiceImpl<ReserveProductMapper, ReserveProduct> implements ReserveProductService {
}
