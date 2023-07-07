package org.makka.greenfarm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.makka.greenfarm.domain.SaleProduct;
import org.makka.greenfarm.domain.SaleProductFavorite;

import java.util.List;

@Mapper
public interface SaleProductFavoriteMapper extends BaseMapper<SaleProductFavorite> {
    @Select("select sale_product_favorite.fid,sale_product_favorite.uid,sale_product_favorite.spid,sale_product_favorite.favoriteDate," +
            "sale_product.picture,sale_product.shelves,sale_product.fid as farmId,sale_product.name,sale_product.uniprice" +
            " from sale_product_favorite,sale_product " +
            "where sale_product_favorite.uid = #{uid} and sale_product_favorite.spid = sale_product.spid")
    List<SaleProductFavorite> getSaleFavoriteList(String uid);
}
