package org.makka.greenfarm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.makka.greenfarm.domain.FarmComment;
import org.makka.greenfarm.domain.FarmFavorite;

import java.util.List;

@Mapper
public interface FarmFavoriteMapper extends BaseMapper<FarmFavorite> {
}
