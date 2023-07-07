package org.makka.greenfarm.controller;

import cn.dev33.satoken.stp.StpUtil;
import org.makka.greenfarm.common.CommonResponse;
import org.makka.greenfarm.domain.Order;
import org.makka.greenfarm.domain.Product;
import org.makka.greenfarm.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("")
    public CommonResponse<List<Order>> initOrder(@RequestBody List<Product> productList, @RequestParam String aid) {
        // Return the token to the frontend
        if (StpUtil.isLogin()) {
            List<Order> OrderList = orderService.initOrder(productList,aid);
            return CommonResponse.creatForSuccess(OrderList);
        } else {
            // 令牌无效或解码错误
            return CommonResponse.creatForError("请先登录");
        }
    }
}
