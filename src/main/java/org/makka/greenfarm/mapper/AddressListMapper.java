package org.makka.greenfarm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.makka.greenfarm.domain.AddressList;
import org.makka.greenfarm.domain.FarmComment;

import java.util.List;

@Mapper
public interface AddressListMapper extends BaseMapper<AddressList> {
    @Select("select aid, uid, province, phone, name, isDefault, city, detail, longitude,latitude" +
            " from addresslist " +
            "where aid = #{aid}")
    public AddressList getBasicAddressDetailsByAid(String aid);
}
