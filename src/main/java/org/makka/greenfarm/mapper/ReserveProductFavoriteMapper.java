package org.makka.greenfarm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.makka.greenfarm.domain.ReserveProductComment;
import org.makka.greenfarm.domain.ReserveProductFavorite;

import java.util.List;

@Mapper
public interface ReserveProductFavoriteMapper extends BaseMapper<ReserveProductFavorite> {
    @Select("select reserve_product_favorite.fid,reserve_product_favorite.uid,reserve_product_favorite.rpid,reserve_product_favorite.favoriteDate," +
            "reserve_product.picture,reserve_product.choice,reserve_product.fid as farmId,reserve_product.name,reserve_product.uniprice" +
            " from reserve_product_favorite,reserve_product " +
            "where reserve_product_favorite.rpid = reserve_product.rpid and reserve_product_favorite.uid = #{uid}")
    List<ReserveProductFavorite> getReserveFavoriteList(String uid);
}
