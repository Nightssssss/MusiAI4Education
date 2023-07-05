package org.makka.greenfarm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.makka.greenfarm.domain.SaleProduct;
import org.makka.greenfarm.domain.SaleProductFavorite;
@Mapper
public interface SaleProductFavoriteMapper extends BaseMapper<SaleProductFavorite> {
}
