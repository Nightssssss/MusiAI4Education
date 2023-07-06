package org.makka.greenfarm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.makka.greenfarm.domain.ReserveProduct;
import org.makka.greenfarm.domain.ReserveProductComment;
import org.makka.greenfarm.mapper.ReserveProductMapper;
import org.makka.greenfarm.service.ReserveProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReserveProductServiceImpl extends ServiceImpl<ReserveProductMapper, ReserveProduct> implements ReserveProductService {

    @Autowired
    private ReserveProductMapper reserveProductMapper;

    @Override
    public List<ReserveProduct> getReserveProductsByFarmId(String fid) {
        //使用reserveProductMapper获取对应农场可种植农产品列表
        QueryWrapper<ReserveProduct> wrapper = new QueryWrapper<>();
        wrapper.eq("fid", fid);
        return reserveProductMapper.selectList(wrapper);
    }

    @Override
    public ReserveProduct getReserveProductDetail(String productId) {
        //获取可种植农产品详细信息
        ReserveProduct reserveProduct = reserveProductMapper.selectById(productId);
        System.out.println("the reserve porduct is"+reserveProduct);
        //设置评论
        reserveProduct.setReserveProductCommentList(getReserveProductComment(productId));
        System.out.println("the reserve porduct is"+reserveProduct);
        return reserveProduct;
    }

    public List<ReserveProductComment> getReserveProductComment(String productId) {
        //级联查询评论信息，获取评论人的信息
        List<ReserveProductComment> commentList = reserveProductMapper.getReserveProductComment(productId);
        return commentList;
    }
}
