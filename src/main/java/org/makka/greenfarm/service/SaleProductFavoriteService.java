package org.makka.greenfarm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.makka.greenfarm.domain.ReserveProductFavorite;
import org.makka.greenfarm.domain.SaleProductFavorite;

import java.util.List;

public interface SaleProductFavoriteService extends IService<SaleProductFavorite> {
    //添加收藏
    public List<SaleProductFavorite> addSaleProductFavorite(String uid, String spid);
    //取消收藏
    public List<SaleProductFavorite> cancelSaleProductFavorite(String uid, String spid);

    public List<SaleProductFavorite> getSaleFavoriteList(String uid);
}
