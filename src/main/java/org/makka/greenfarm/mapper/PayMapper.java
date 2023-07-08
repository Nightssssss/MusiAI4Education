package org.makka.greenfarm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.makka.greenfarm.domain.Pay;

@Mapper
public interface PayMapper extends BaseMapper<Pay> {
}
