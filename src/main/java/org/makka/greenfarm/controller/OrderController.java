package org.makka.greenfarm.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.makka.greenfarm.common.CommonResponse;
import org.makka.greenfarm.domain.Order;
import org.makka.greenfarm.domain.Product;
import org.makka.greenfarm.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    //新增订单
    @PostMapping("")
    public CommonResponse<List<Order>> initOrder(@RequestBody Map<String,Object> map) {
        // Return the token to the frontend
        if (StpUtil.isLogin()) {

            Object productInfo =  map.get("productList");
            String json = JSONUtil.toJsonStr(productInfo);

            ObjectMapper objectMapper = new ObjectMapper();
            try {
                List<Product> productList = objectMapper.readValue(json, new TypeReference<List<Product>>() {});
                String aid = (String) map.get("aid");
                List<Order> OrderList = orderService.initOrder(productList,aid);
                System.out.println(OrderList);
                return CommonResponse.creatForSuccess(OrderList);
            }catch(Exception e){
                e.printStackTrace();
            }
            return null;
        } else {
            // 令牌无效或解码错误
            return CommonResponse.creatForError("请先登录");
        }
    }

    //查询所有订单
    @GetMapping("")
    public CommonResponse<List<Order>> searchOrder() {
        // Return the token to the frontend
        if (StpUtil.isLogin()) {
            List<Order> orderList = orderService.selectOrder();
            System.out.println(orderList);
            System.out.println(orderService.selectOrder());
            return CommonResponse.creatForSuccess(orderList);
        } else {
            // 令牌无效或解码错误
            return CommonResponse.creatForError("请先登录");
        }
    }

    @PutMapping("/reserve")
    public CommonResponse<List<Order>> updateReserveOrderStatus(@RequestParam String oid) {
        // Return the token to the frontend
        if (StpUtil.isLogin()) {
            List<Order>orderList = orderService.updateReserveOrdersStatusByOrderId(oid);
            return CommonResponse.creatForSuccess(orderList);
        } else {
            // 令牌无效或解码错误
            return CommonResponse.creatForError("请先登录");
        }
    }

    @PutMapping("/sale")
    public CommonResponse<List<Order>> updateSaleOrderStatus(@RequestParam String oid) {
        // Return the token to the frontend
        if (StpUtil.isLogin()) {
            List<Order>orderList = orderService.updateSaleOrdersStatusByOrderId(oid);
            return CommonResponse.creatForSuccess(orderList);
        } else {
            // 令牌无效或解码错误
            return CommonResponse.creatForError("请先登录");
        }
    }

}
