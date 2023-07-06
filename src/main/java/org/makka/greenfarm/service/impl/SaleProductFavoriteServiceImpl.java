package org.makka.greenfarm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.makka.greenfarm.common.CommonResponse;
import org.makka.greenfarm.domain.ReserveProductFavorite;
import org.makka.greenfarm.domain.SaleProductFavorite;
import org.makka.greenfarm.domain.User;
import org.makka.greenfarm.mapper.ReserveProductFavoriteMapper;
import org.makka.greenfarm.mapper.SaleProductFavoriteMapper;
import org.makka.greenfarm.service.SaleProductFavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static org.makka.greenfarm.common.CommonResponse.creatForError;

@Service
public class SaleProductFavoriteServiceImpl extends ServiceImpl<SaleProductFavoriteMapper, SaleProductFavorite> implements SaleProductFavoriteService {
    @Autowired
    private SaleProductFavoriteMapper saleProductFavoriteMapper;

    @Override
    public List<SaleProductFavorite> addSaleProductFavorite(String uid, String spid) {

        //如果该用户已经为该产品添加收藏，则不可重复收藏
        QueryWrapper<SaleProductFavorite> wrapper = new QueryWrapper<>();
        wrapper.eq("spid", spid);
        SaleProductFavorite saleProductFavorite = saleProductFavoriteMapper.selectOne(wrapper);
        if (saleProductFavorite != null) {
            return null;
        } else {
            SaleProductFavorite saleProductFavorite1 = new SaleProductFavorite();
            //插入timestamp类型的当期时间
            saleProductFavorite1.setFavoriteDate(LocalDateTime.now());
            String fid = String.valueOf(System.currentTimeMillis());
            saleProductFavorite1.setFid(fid);
            saleProductFavorite1.setUid(uid);
            saleProductFavorite1.setSpid(spid);
            baseMapper.insert(saleProductFavorite1);
            //获取该用户的所有收藏
            return saleProductFavoriteMapper.getSaleFavoriteList(uid);
        }
    }
    @Override
    public List<SaleProductFavorite> cancelSaleProductFavorite(String uid, String spid) {
        //根据spid删除该商品的收藏
        QueryWrapper<SaleProductFavorite> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("spid", spid);
        saleProductFavoriteMapper.delete(queryWrapper);
        //获取该用户的所有收藏
        return saleProductFavoriteMapper.getSaleFavoriteList(uid);
    }
}
