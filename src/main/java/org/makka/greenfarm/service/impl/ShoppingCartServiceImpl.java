package org.makka.greenfarm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.makka.greenfarm.domain.ShoppingCart;
import org.makka.greenfarm.mapper.ShoppingCartMapper;
import org.makka.greenfarm.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    public List<ShoppingCart> getShoppingCartByUid(String uid) {
        //获取购物车信息
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("uid", uid);
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.selectList(wrapper);
        return shoppingCartList;
    }
}
