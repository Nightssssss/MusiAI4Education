package org.makka.greenfarm.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.makka.greenfarm.domain.SaleProduct;
import org.makka.greenfarm.mapper.SaleProductMapper;
import org.makka.greenfarm.service.SaleProductService;
import org.springframework.stereotype.Service;

@Service
public class SaleProductServiceImpl extends ServiceImpl<SaleProductMapper, SaleProduct> implements SaleProductService {
}
