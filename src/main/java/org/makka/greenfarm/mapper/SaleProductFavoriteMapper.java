package org.makka.greenfarm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.makka.greenfarm.domain.SaleProduct;
import org.makka.greenfarm.domain.SaleProductFavorite;

import java.util.List;

@Mapper
public interface SaleProductFavoriteMapper extends BaseMapper<SaleProductFavorite> {
    @Select("select sale_product_favorite.fid,sale_product_favorite.uid,sale_product_favorite.spid,sale_product_favorite.favoriteDate" +
            " from sale_product_favorite " +
            "where sale_product_favorite.uid = #{uid}")
    List<SaleProductFavorite> getSaleFavoriteList(String uid);
}
