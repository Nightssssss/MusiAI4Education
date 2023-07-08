package org.makka.greenfarm.service;

import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.service.IService;
import org.makka.greenfarm.domain.Order;
import org.makka.greenfarm.domain.Product;
import org.makka.greenfarm.domain.ReserveProduct;
import org.makka.greenfarm.domain.SaleProduct;

import java.util.List;

public interface OrderService extends IService<Order> {
    //获得订单中的地址，生成初步订单
    public List<Order> initOrder(List<Product> productList, String aid);

    //根据可种植农产品ID查询在售农产品单价
    public ReserveProduct searchReserveProductUnitPriceById(String rpid);

    //根据在售农产品ID查询在售农产品单价
    public SaleProduct searchSaleProductUnitPriceById(String spid);

    //查询所有订单
    public List<Order> selectOrder();

    //查询同一个订单
    public List<Order> selectOrdersByOrderId(String oid);
}
