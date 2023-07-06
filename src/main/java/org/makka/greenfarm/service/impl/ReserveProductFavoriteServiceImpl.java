package org.makka.greenfarm.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.makka.greenfarm.domain.ReserveProductComment;
import org.makka.greenfarm.domain.ReserveProductFavorite;
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
        ReserveProductFavorite reserveProductFavorite = new ReserveProductFavorite();
        //插入timestamp类型的当期时间
        reserveProductFavorite.setFavoriteTime(LocalDateTime.now());
        String fid = String.valueOf(System.currentTimeMillis());
        reserveProductFavorite.setFid(fid);
        reserveProductFavorite.setUid(uid);
        reserveProductFavorite.setRpid(rpid);
        baseMapper.insert(reserveProductFavorite);
        //获取该用户的所有收藏
        List<ReserveProductFavorite> favoriteList = reserveProductFavoriteMapper.getReserveFavoriteList(uid);
        return favoriteList;
    }
}
