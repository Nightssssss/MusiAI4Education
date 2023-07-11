package org.makka.greenfarm.service.impl;

import com.alibaba.fastjson.JSON;
import org.makka.greenfarm.BlockChain.Block;
import org.makka.greenfarm.BlockChain.BlockCache;
import org.makka.greenfarm.BlockChain.BlockConstant;
import org.makka.greenfarm.BlockChain.Transaction;
import org.makka.greenfarm.domain.Message;
import org.makka.greenfarm.domain.Order;
import org.makka.greenfarm.mapper.OrderMapper;
import org.makka.greenfarm.service.BlockService;
import org.makka.greenfarm.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 共识机制
 * 采用POW即工作量证明实现共识
 *
 */
@Service
public class PowServiceImpl {

    @Autowired
    private BlockCache blockCache;

    @Autowired
    private BlockService blockService;

    @Autowired
    private P2PServiceImpl p2PService;

    /**
     * 通过“挖矿”进行工作量证明，实现节点间的共识
     *
     * @return
     * @throws UnknownHostException
     */
    public Block mine(List<Order> orderList){

        // 封装业务数据集合，记录区块产生的节点信息，临时硬编码实现
        List<Transaction> tsaList = new ArrayList<Transaction>();
        Transaction tsa1 = new Transaction();
        tsa1.setId("1");
        tsa1.setBusinessInfo("IP："+CommonUtil.getLocalIp()+"，port："+blockCache.getP2pport()+"");
        tsaList.add(tsa1);
        Transaction tsa2 = new Transaction();
        tsa2.setId("2");
        tsa2.setBusinessInfo("block height："+(blockCache.getLatestBlock().getIndex()+1));
        tsaList.add(tsa2);
        Transaction tsa3 = new Transaction();
        tsa3.setOrderList(orderList);
        tsaList.add(tsa3);

        // 定义每次哈希函数的结果
        String newBlockHash = "";
        int nonce = 0;
        long start = System.currentTimeMillis();
        System.out.println("start");
        while (true) {
            // 计算新区块hash值
            newBlockHash = blockService.calculateHash(blockCache.getLatestBlock().getHash(), tsaList, nonce);
            // 校验hash值
            if (blockService.isValidHash(newBlockHash)) {
                System.out.println("hash：" + newBlockHash);
                System.out.println("cost time：" + (System.currentTimeMillis() - start) + "ms");
                break;
            }
//            System.out.println("第"+(nonce+1)+"次尝试计算的hash值：" + newBlockHash);
            nonce++;
        }
        // 创建新的区块
        Block block = blockService.createNewBlock(nonce, blockCache.getLatestBlock().getHash(), newBlockHash, tsaList);

        //创建成功后，全网广播出去
        Message msg = new Message();
        msg.setType(BlockConstant.RESPONSE_LATEST_BLOCK);
        msg.setData(JSON.toJSONString(block));
        p2PService.broatcast(JSON.toJSONString(msg));

        return block;
    }
}
