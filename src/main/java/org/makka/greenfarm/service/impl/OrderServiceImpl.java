package org.makka.greenfarm.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.makka.greenfarm.domain.*;
import org.makka.greenfarm.mapper.OrderMapper;
import org.makka.greenfarm.mapper.ReserveProductMapper;
import org.makka.greenfarm.mapper.SaleProductMapper;
import org.makka.greenfarm.mapper.UserMapper;
import org.makka.greenfarm.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private ReserveProductMapper reserveProductMapper;

    @Autowired
    private SaleProductMapper saleProductMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public List<Order> initOrder(List<Product> productList, String aid) {

        List<Order> orderList = new ArrayList<Order>(productList.size());

        String oid = String.valueOf(System.currentTimeMillis());

        for (Product product : productList) {

            if (product.getType() == 0) {

                Order order = new Order();
                String uid = StpUtil.getLoginIdAsString();
                order.setUid(uid);
                order.setAid(aid);
                order.setOid(oid);

                Date orderDate = new Date();
                order.setOrderDate(orderDate);
                order.setStatus(0);

                // 4.1 如果是在售农产品，获取对应的产品信息，存入在售农产品列表
                String pid = product.getProductId();
                order.setPid(pid);

                SaleProduct saleProduct = searchSaleProductUnitPriceById(product.getProductId());
                order.setUniprice(saleProduct.getUniprice());

                order.setQuantity(product.getQuantity());
                order.setType(product.getType());

                System.out.println(order);

                orderMapper.insert(order);

                orderList.add(order);

            } else {

                Order order = new Order();

                String uid = StpUtil.getLoginIdAsString();

                order.setUid(uid);
                order.setAid(aid);
                order.setOid(oid);

                Date orderDate = new Date();
                order.setOrderDate(orderDate);
                order.setStatus(0);

                // 4.2 如果是可种植农产品，获取对应的产品信息，存入可种植农产品列表
                String pid = product.getProductId();
                order.setPid(pid);
                order.setQuantity(product.getQuantity());
                order.setType(product.getType());
                ReserveProduct reserveProduct = searchReserveProductUnitPriceById(product.getProductId());
                order.setUniprice(reserveProduct.getUniprice());

                System.out.println(order);

                baseMapper.insert(order);

                orderList.add(order);
            }
        }
        return orderList;
    }

    @Override
    public ReserveProduct searchReserveProductUnitPriceById(String rpid) {
        QueryWrapper<ReserveProduct> wrapper = new QueryWrapper<>();
        wrapper.eq("rpid", rpid);
        return reserveProductMapper.selectOne(wrapper);
    }

    @Override
    public SaleProduct searchSaleProductUnitPriceById(String spid) {
        QueryWrapper<SaleProduct> wrapper = new QueryWrapper<>();
        wrapper.eq("spid", spid);
        return saleProductMapper.selectOne(wrapper);
    }

    @Override
    public List<Order> selectOrder() {
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        return orderMapper.selectList(wrapper);
    }

    @Override
    public List<Order> selectOrdersByOrderId(String oid) {
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("oid", oid);
        return orderMapper.selectList(wrapper);
    }

    @Override
    public List<Order> updateOrdersStatusByOrderId(String oid) {
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("oid", oid);
        List<Order> orderList = orderMapper.selectList(wrapper);
        System.out.println(orderList);
        for(Order order:orderList){
            order.setStatus(1);
            //这里无法更新
            orderMapper.updateById(order);
        }
        return orderMapper.selectList(wrapper);
    }
}
