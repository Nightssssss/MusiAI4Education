package org.makka.greenfarm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.makka.greenfarm.domain.FarmFavorite;
import org.makka.greenfarm.domain.ReserveProductFavorite;
import org.makka.greenfarm.mapper.FarmFavoriteMapper;
import org.makka.greenfarm.mapper.ReserveProductFavoriteMapper;
import org.makka.greenfarm.service.FarmFavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FarmFavoriteServiceImpl extends ServiceImpl<FarmFavoriteMapper, FarmFavorite> implements FarmFavoriteService {

    @Autowired
    private FarmFavoriteMapper farmFavoriteMapper;

    @Override
    public List<FarmFavorite> addFarmFavorite(String uid, String fid) {
        //如果该用户已经为该农场添加收藏，则不可重复收藏
        QueryWrapper<FarmFavorite> wrapper = new QueryWrapper<>();
        wrapper.eq("fid", fid);
        wrapper.eq("uid", uid);
        FarmFavorite farmFavorite = farmFavoriteMapper.selectOne(wrapper);
        if (farmFavorite != null) {
            return null;
        } else {
            FarmFavorite farmFavorite1 = new FarmFavorite();
            //插入timestamp类型的当期时间
            farmFavorite1.setFavoriteDate(LocalDateTime.now());
            String ffid = String.valueOf(System.currentTimeMillis());
            farmFavorite1.setFfid(ffid);
            farmFavorite1.setUid(uid);
            farmFavorite1.setFid(fid);
            baseMapper.insert(farmFavorite1);
            //获取该用户的所有收藏
            return farmFavoriteMapper.getFarmFavoriteList(uid);
        }
    }

    @Override
    public List<FarmFavorite> cancelFarmFavorite(String uid, String ffid) {
        QueryWrapper<FarmFavorite> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ffid", ffid);
        farmFavoriteMapper.delete(queryWrapper);
        //获取该用户的所有收藏
        return farmFavoriteMapper.getFarmFavoriteList(uid);
    }

    @Override
    public List<FarmFavorite> getFarmFavoriteList(String uid) {
        return farmFavoriteMapper.getFarmFavoriteList(uid);
    }
}
