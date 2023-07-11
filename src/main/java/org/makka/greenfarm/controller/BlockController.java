package org.makka.greenfarm.controller;

import com.alibaba.fastjson.JSON;
import org.makka.greenfarm.domain.Order;
import org.makka.greenfarm.service.BlockService;
import org.makka.greenfarm.service.impl.PowServiceImpl;
import org.makka.greenfarm.BlockChain.BlockCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class BlockController {

    @Resource
    BlockService blockService;

    @Autowired
    BlockCache blockCache;

    @Autowired
    PowServiceImpl powService;

    /**
     * 查看当前节点区块链数据
     * @return
     */
    @GetMapping("/scan")
    @ResponseBody
    public String scanBlock() {
        return JSON.toJSONString(blockCache.getBlockChain());
    }

    /**
     * 查看当前节点区块链数据
     * @return
     */
    @GetMapping("/data")
    @ResponseBody
    public String scanData() {
        return JSON.toJSONString(blockCache.getPackedTransactions());
    }

    /**
     * 创建创世区块
     * @return
     */
    @GetMapping("/create")
    @ResponseBody
    public String createFirstBlock() {
        blockService.createGenesisBlock();
        return JSON.toJSONString(blockCache.getBlockChain());
    }

    /**
     * 工作量证明PoW
     * 挖矿生成新的区块
     */
    @GetMapping("/mine")
    @ResponseBody
    public String createNewBlock(List<Order> orderList) {
        powService.mine(orderList);
        return JSON.toJSONString(blockCache.getBlockChain());
    }
}
