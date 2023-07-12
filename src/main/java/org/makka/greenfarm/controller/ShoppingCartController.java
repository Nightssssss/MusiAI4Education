package org.makka.greenfarm.controller;

import cn.dev33.satoken.stp.StpUtil;
import org.makka.greenfarm.common.CommonResponse;
import org.makka.greenfarm.domain.ShoppingCart;
import org.makka.greenfarm.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/shoppingcarts")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;

}
