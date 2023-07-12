package org.makka.greenfarm.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.makka.greenfarm.domain.*;
import org.makka.greenfarm.mapper.*;
import org.makka.greenfarm.service.FarmService;
import org.makka.greenfarm.service.OrderService;
import org.makka.greenfarm.service.ReserveProductService;
import org.makka.greenfarm.service.SaleProductService;
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
    @Autowired
    private ReserveProductService reserveProductService;
    @Autowired
    private SaleProductService saleProductService;
    @Autowired
    private FarmMapper farmMapper;


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

                //添加可种植农产品在农场中的经纬度
                order.setLongitude(product.getLongitude());
                order.setLatitude(product.getLatitude());

                baseMapper.insert(order);
                orderList.add(order);
            }
        }
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("oid", oid);
        for(Order order:orderList){
            order.setAddressList(addressListMapper.getBasicAddressDetailsByAid(order.getAid()));
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
            if(order.getType()==0){
                //是在售农产品
                saleProductService.updateSaleProductsStatusBySpid(order.getPid(),order.getQuantity());
            }else{
                //是可种植农产品
                reserveProductService.updateReserveProductsStatusByRpid(order.getPid(),order.getQuantity());
            }
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

    @Override
    public List<Order> getSaleOrdersByOwnerId(String ownerid) {
        QueryWrapper<Farm> wrapper = new QueryWrapper<>();
        wrapper.eq("ownerid", ownerid);
        Farm farm = farmMapper.selectOne(wrapper);
        String fid = farm.getFid();

        // 查找fid对应的pid
        QueryWrapper<SaleProduct> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("fid", fid);
        List<SaleProduct> saleProductList = saleProductMapper.selectList(wrapper1);

        List<Order> orderList = new ArrayList<>();
        // 查找pid对应的orderList
        for (SaleProduct saleProduct : saleProductList) {
            QueryWrapper<Order> wrapper2 = new QueryWrapper<>();
            wrapper2.eq("pid", saleProduct.getSpid());
            List<Order> orderListTmp = orderMapper.selectList(wrapper2);
            for (Order order : orderListTmp) {
                QueryWrapper<AddressList> wrapper3 = new QueryWrapper<>();
                wrapper3.eq("aid", order.getAid());
                order.setAddressList(addressListMapper.selectOne(wrapper3));
                order.setImage(saleProduct.getPicture());
            }
            orderList.addAll(orderListTmp);
        }
        return orderList;
    }

    @Override
    public List<Order> getReserveOrdersByOwnerId(String ownerid) {
        QueryWrapper<Farm> wrapper = new QueryWrapper<>();
        wrapper.eq("ownerid", ownerid);
        Farm farm = farmMapper.selectOne(wrapper);
        String fid = farm.getFid();

        // 查找fid对应的pid
        QueryWrapper<ReserveProduct> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("fid", fid);
        List<ReserveProduct> reserveProductList = reserveProductMapper.selectList(wrapper1);

        List<Order> orderList = new ArrayList<>();
        // 查找pid对应的orderList
        for (ReserveProduct reserveProduct : reserveProductList) {
            QueryWrapper<Order> wrapper2 = new QueryWrapper<>();
            wrapper2.eq("pid", reserveProduct.getRpid());
            List<Order> orderListTmp = orderMapper.selectList(wrapper2);
            for (Order order : orderListTmp) {
                QueryWrapper<AddressList> wrapper3 = new QueryWrapper<>();
                wrapper3.eq("aid", order.getAid());
                order.setAddressList(addressListMapper.selectOne(wrapper3));
                order.setImage(reserveProduct.getPicture());
            }
            orderList.addAll(orderListTmp);
        }
        return orderList;
    }
}
