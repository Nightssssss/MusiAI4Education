package org.makka.greenfarm.utils;

import com.alibaba.fastjson.JSON;
import org.makka.greenfarm.BlockChain.BlockCache;
import org.makka.greenfarm.domain.Order;
import org.makka.greenfarm.service.impl.PowServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BlockUtil {
    @Autowired
    private static PowServiceImpl powService;

    @Autowired
    private static BlockCache blockCache;

    public static String createNewBlock(List<Order> orderList) {
        powService.mine(orderList);
        return JSON.toJSONString(blockCache.getBlockChain());
    }
}
