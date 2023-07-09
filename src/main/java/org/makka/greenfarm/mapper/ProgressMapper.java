package org.makka.greenfarm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.makka.greenfarm.domain.Progress;

import java.util.List;

@Mapper
public interface ProgressMapper extends BaseMapper<Progress> {
    @Select("select progress.*,reserve_product.picture,reserve_product.name,reserve_product.fid" +
            " from progress,reserve_product " +
            "where progress.rpid = reserve_product.rpid and progress.uid = #{uid}")
    public List<Progress> getProgressList(String uid);

    @Select("select progress.*,reserve_product.picture,reserve_product.name,reserve_product.fid" +
            " from progress,reserve_product " +
            "where progress.rpid = reserve_product.rpid and progress.pid=#{pid}")
    public Progress getProgress(String pid);
}
