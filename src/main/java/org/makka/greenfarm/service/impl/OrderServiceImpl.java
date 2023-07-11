package org.makka.greenfarm.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.makka.greenfarm.domain.*;
import org.makka.greenfarm.mapper.AddressListMapper;
import org.makka.greenfarm.mapper.OrderMapper;
import org.makka.greenfarm.mapper.ReserveProductMapper;
import org.makka.greenfarm.mapper.SaleProductMapper;
import org.makka.greenfarm.service.OrderService;
import org.mockito.internal.matchers.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
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
    @Autowired
    private AddressListMapper addressListMapper;

    @Override
    public List<Order> initOrder(List<Product> productList, AddressList addressList) {
        List<Order> orderList = new ArrayList<Order>(productList.size());
        String oid = String.valueOf(System.currentTimeMillis());
        String aid = addressList.getAid();
        for (Product product : productList) {
            if (product.getType() == 0) {
                Order order = new Order();
                String uid = StpUtil.getLoginIdAsString();
                order.setUid(uid);
                order.setOid(oid);
                order.setAid(aid);
                Date orderDate = new Date();
                order.setOrderDate(orderDate);
                order.setStatus("未支付");
                // 4.1 如果是在售农产品，获取对应的产品信息，存入在售农产品列表
                String pid = product.getProductId();
                order.setPid(pid);
                SaleProduct saleProduct = searchSaleProductUnitPriceById(product.getProductId());
                order.setUniprice(saleProduct.getUniprice());
                order.setQuantity(product.getQuantity());
                order.setType(product.getType());
                orderMapper.insert(order);
                orderList.add(order);

            } else {
                Order order = new Order();
                String uid = StpUtil.getLoginIdAsString();
                order.setUid(uid);
                order.setOid(oid);
                order.setAid(aid);
                Date orderDate = new Date();
                order.setOrderDate(orderDate);
                order.setStatus("未支付");
                // 4.2 如果是可种植农产品，获取对应的产品信息，存入可种植农产品列表
                String pid = product.getProductId();
                order.setPid(pid);
                order.setQuantity(product.getQuantity());
                order.setType(product.getType());
                ReserveProduct reserveProduct = searchReserveProductUnitPriceById(product.getProductId());
                order.setUniprice(reserveProduct.getUniprice());
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

    @Cacheable(value = "orders", key = "1")
    @Override
    public List<Order> selectOrder() {
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        List<Order> orderList = orderMapper.selectList(wrapper);
        for(Order order:orderList){
            order.setAddressList(addressListMapper.getBasicAddressDetailsByAid(order.getAid()));
        }
        return orderList;
    }

    @Cacheable(value = "orders", key = "0")
    @Override
    public List<Order> selectOrdersByOrderId(String oid) {
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("oid", oid);
        List<Order> orderList = orderMapper.selectList(wrapper);
        for(Order order:orderList) {
            order.setAddressList(addressListMapper.getBasicAddressDetailsByAid(order.getAid()));
        }
        return orderList;
    }

    @Override
    public List<Order> updatePayOrdersStatusByOrderId(String oid) {
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("oid", oid);
        List<Order> orderList = orderMapper.selectList(wrapper);
        System.out.println(orderList);
        for(Order order:orderList){
            QueryWrapper<Order> wrapper1 = new QueryWrapper<>();
            order.setStatus("已支付");
            wrapper1.eq("oid", oid);
            wrapper1.eq("pid", order.getPid());
            orderMapper.update(order,wrapper1);
        }
        QueryWrapper<Order> wrapper2 = new QueryWrapper<>();
        wrapper2.eq("oid", oid);
        System.out.println(orderMapper.selectList(wrapper2));
        return orderMapper.selectList(wrapper2);
    }

    @Override
    public List<Order> updateReserveOrdersStatusByOrderId(String oid) {
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("oid", oid);
        List<Order> orderList = orderMapper.selectList(wrapper);
        for(Order order:orderList){
            if(order.getType()==1){
            QueryWrapper<Order> wrapper1 = new QueryWrapper<>();
            order.setStatus("种植中");
            wrapper1.eq("oid", oid);
            wrapper1.eq("pid", order.getPid());
            orderMapper.update(order,wrapper1);
            }
        }
        QueryWrapper<Order> wrapper2 = new QueryWrapper<>();
        wrapper2.eq("oid", oid);
        System.out.println(orderMapper.selectList(wrapper2));
        return orderMapper.selectList(wrapper2);
    }

    @Override
    public List<Order> updateSaleOrdersStatusByOrderId(String oid) {
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("oid", oid);
        List<Order> orderList = orderMapper.selectList(wrapper);
        for(Order order:orderList){
            if(order.getType()==0){
                QueryWrapper<Order> wrapper1 = new QueryWrapper<>();
                order.setStatus("配送中");
                wrapper1.eq("oid", oid);
                wrapper1.eq("pid", order.getPid());
                orderMapper.update(order,wrapper1);
            }
        }
        QueryWrapper<Order> wrapper2 = new QueryWrapper<>();
        wrapper2.eq("oid", oid);
        System.out.println(orderMapper.selectList(wrapper2));
        return orderMapper.selectList(wrapper2);
    }

    @Override
    public List<Order> deleteOrdersByOid(String oid) {
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("oid", oid);
        List<Order> orderList = orderMapper.selectList(wrapper);
        System.out.println(orderList);
        for (Order order:orderList){
            QueryWrapper<Order> wrapper1 = new QueryWrapper<>();
            wrapper1.eq("oid", order.getOid());
            orderMapper.delete(wrapper1);
        }
        QueryWrapper<Order> wrapper1 = new QueryWrapper<>();
        return orderMapper.selectList(wrapper1);
    }


}
