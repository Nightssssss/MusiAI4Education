package org.makka.greenfarm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.makka.greenfarm.domain.User;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
