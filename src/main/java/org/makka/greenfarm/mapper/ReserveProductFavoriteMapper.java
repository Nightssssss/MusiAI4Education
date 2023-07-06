package org.makka.greenfarm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.makka.greenfarm.domain.ReserveProductComment;
import org.makka.greenfarm.domain.ReserveProductFavorite;

import java.util.List;

@Mapper
public interface ReserveProductFavoriteMapper extends BaseMapper<ReserveProductFavorite> {
    @Select("select reserve_product_favorite.fid,reserve_product_favorite.uid,reserve_product_favorite.rpid,reserve_product_favorite.favoriteDate" +
            " from reserve_product_favorite " +
            "where reserve_product_favorite.uid = #{uid}")
    List<ReserveProductFavorite> getReserveFavoriteList(String uid);
}
