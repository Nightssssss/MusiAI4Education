package org.makka.greenfarm.controller;

import org.makka.greenfarm.common.CommonResponse;
import org.makka.greenfarm.domain.Order;
import org.makka.greenfarm.service.OrderService;
import org.makka.greenfarm.service.PayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/alipay")
public class PayController {

    @Autowired
    private PayService payService;

    @Autowired
    private OrderService orderService;

    @GetMapping("/create")
    public String payController(@RequestParam String oid) throws IOException {
        List<Order> orderList = orderService.selectOrdersByOrderId(oid);
        return payService.getPay(orderList);
    }

    // 处理支付宝回调URL的方法
    @RequestMapping("/return")
    public CommonResponse<List<Order>> handleReturn(@RequestParam("orderId") String oid) {
        System.out.println(oid);
        // 更新数据库中的订单状态
        List<Order> orderList = orderService.updatePayOrdersStatusByOrderId(oid);

        return CommonResponse.creatForSuccess(orderList);
    }

    // 处理支付宝异步通知URL的方法
    @RequestMapping("/notify")
    public String handleNotify() {
        // 处理支付宝异步通知的业务逻辑
        return "Payment notification";
    }

}