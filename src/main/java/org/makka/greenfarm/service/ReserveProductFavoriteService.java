package org.makka.greenfarm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.makka.greenfarm.domain.ReserveProductFavorite;
import org.makka.greenfarm.domain.SaleProductComment;
import org.makka.greenfarm.domain.SaleProductFavorite;

import java.util.List;
import java.util.Set;

public interface ReserveProductFavoriteService extends IService<ReserveProductFavorite> {
    //添加收藏
    public List<ReserveProductFavorite> ReserveProductFavorite(String uid, String rpid);
    //取消收藏
    public List<ReserveProductFavorite> cancelReserveProductFavorite(String uid, String rpid);
    //获得收藏
    public List<ReserveProductFavorite> getReserveFavoriteList(String uid);

}
