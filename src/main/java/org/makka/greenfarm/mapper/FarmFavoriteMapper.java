package org.makka.greenfarm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.makka.greenfarm.domain.FarmComment;
import org.makka.greenfarm.domain.FarmFavorite;

import java.util.List;

@Mapper
public interface FarmFavoriteMapper extends BaseMapper<FarmFavorite> {

    //    收藏农场编号|收藏农场用户ID|农场编号|收藏日期|农场图片|农场评分|农场名|农场描述
    @Select("select farm_favorite.ffid,farm_favorite.uid,farm_favorite.fid,farm_favorite.favoriteDate," +
            "farm.picture,farm.score,farm.name,farm.description" +
            " from farm_favorite,farm " +
            "where farm_favorite.fid = farm.fid and farm_favorite.uid = #{uid}")
    List<FarmFavorite> getFarmFavoriteList(String uid);
}
