package org.makka.greenfarm.utils;

import com.alibaba.fastjson.JSON;
import org.makka.greenfarm.BlockChain.BlockCache;
import org.makka.greenfarm.domain.Order;
import org.makka.greenfarm.service.BlockService;
import org.makka.greenfarm.service.impl.PowServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BlockUtil {
    @Autowired
    private PowServiceImpl powService;

    @Autowired
    private BlockCache blockCache;

    @Autowired
    private BlockService blockService;
    public String createNewBlock(List<Order> orderList) {
        if (blockCache.getBlockChain().size() == 0) {
            blockService.createGenesisBlock();
        }
        powService.mine(orderList);
        return JSON.toJSONString(blockCache.getBlockChain());
    }
}
