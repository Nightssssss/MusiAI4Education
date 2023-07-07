package org.makka.greenfarm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.makka.greenfarm.domain.ReserveProductComment;
import org.makka.greenfarm.domain.ReserveProductFavorite;
import org.makka.greenfarm.domain.SaleProductFavorite;
import org.makka.greenfarm.mapper.ReserveProductCommentMapper;
import org.makka.greenfarm.mapper.ReserveProductFavoriteMapper;
import org.makka.greenfarm.service.ReserveProductFavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReserveProductFavoriteServiceImpl extends ServiceImpl<ReserveProductFavoriteMapper, ReserveProductFavorite> implements ReserveProductFavoriteService {
    @Autowired
    private ReserveProductFavoriteMapper reserveProductFavoriteMapper;

    @Override
    public List<ReserveProductFavorite> ReserveProductFavorite(String uid, String rpid) {

        //如果该用户已经为该产品添加收藏，则不可重复收藏
        QueryWrapper<ReserveProductFavorite> wrapper = new QueryWrapper<>();
        wrapper.eq("rpid", rpid);
        ReserveProductFavorite reserveProductFavorite = reserveProductFavoriteMapper.selectOne(wrapper);
        if (reserveProductFavorite != null) {
            return null;
        } else {
            ReserveProductFavorite reserveProductFavorite1 = new ReserveProductFavorite();
            //插入timestamp类型的当期时间
            reserveProductFavorite1.setFavoriteDate(LocalDateTime.now());
            String fid = String.valueOf(System.currentTimeMillis());
            reserveProductFavorite1.setFid(fid);
            reserveProductFavorite1.setUid(uid);
            reserveProductFavorite1.setRpid(rpid);
            baseMapper.insert(reserveProductFavorite1);
            //获取该用户的所有收藏
            return reserveProductFavoriteMapper.getReserveFavoriteList(uid);
        }
    }
    @Override
    public List<ReserveProductFavorite> cancelReserveProductFavorite(String uid, String rpid) {
        //根据spid删除该商品的收藏
        QueryWrapper<ReserveProductFavorite> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("rpid", rpid);
        reserveProductFavoriteMapper.delete(queryWrapper);
        //获取该用户的所有收藏
        return reserveProductFavoriteMapper.getReserveFavoriteList(uid);
    }
}
