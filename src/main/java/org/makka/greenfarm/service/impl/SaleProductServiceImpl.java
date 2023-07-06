package org.makka.greenfarm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.makka.greenfarm.domain.ReserveProduct;
import org.makka.greenfarm.domain.ReserveProductComment;
import org.makka.greenfarm.domain.SaleProduct;
import org.makka.greenfarm.domain.SaleProductComment;
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

    @Override
    public SaleProduct getSaleProductDetail(String productId) {
        //获取可种植农产品详细信息
        SaleProduct saleProduct = saleProductMapper.selectById(productId);
        System.out.println("the sale porduct is"+saleProduct);
        //设置评论
        saleProduct.setSaleProductCommentList(getSaleProductComment(productId));
        System.out.println("the sale porduct is"+saleProduct);
        return saleProduct;
    }

    public List<SaleProductComment> getSaleProductComment(String productId) {
        //级联查询评论信息，获取评论人的信息
        List<SaleProductComment> commentList = saleProductMapper.getSaleProductComment(productId);
        return commentList;
    }

}
