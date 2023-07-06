package org.makka.greenfarm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.makka.greenfarm.domain.ReserveProduct;
import org.makka.greenfarm.domain.SaleProduct;
import org.makka.greenfarm.mapper.ReserveProductMapper;
import org.makka.greenfarm.mapper.SaleProductMapper;
import org.makka.greenfarm.service.SaleProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SaleProductServiceImpl extends ServiceImpl<SaleProductMapper, SaleProduct> implements SaleProductService {


    @Autowired
    private SaleProductMapper saleProductMapper;

    @Override
    public List<SaleProduct> getSaleProductsByFarmId(String fid) {
        //使用reserveProductMapper获取对应农场可种植农产品列表
        QueryWrapper<SaleProduct> wrapper = new QueryWrapper<>();
        wrapper.eq("fid", fid);
        return saleProductMapper.selectList(wrapper);
    }

}
