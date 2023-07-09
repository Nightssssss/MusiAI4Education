package org.makka.greenfarm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.makka.greenfarm.domain.FarmFavorite;
import org.makka.greenfarm.domain.ReserveProductFavorite;

import java.util.List;

public interface FarmFavoriteService extends IService<FarmFavorite> {
    //添加收藏
    public List<FarmFavorite> addFarmFavorite(String uid, String fid);
    //取消收藏
    public List<FarmFavorite> cancelFarmFavorite(String uid, String fid);
    //获得收藏
    public List<FarmFavorite> getFarmFavoriteList(String uid);

}
