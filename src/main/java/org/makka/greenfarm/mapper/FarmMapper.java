package org.makka.greenfarm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.makka.greenfarm.domain.Farm;
@Mapper
public interface FarmMapper extends BaseMapper<Farm> {
}
