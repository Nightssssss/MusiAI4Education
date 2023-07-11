package org.makka.greenfarm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.makka.greenfarm.domain.Order;

import java.util.List;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {
    @Select("select orders.* from orders,sale_product where orders.pid = sale_product.spid and sale_product.fid = #{fid}")
    public List<Order> getSaleOrdersByFid(String fid);
}
