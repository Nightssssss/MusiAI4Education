package org.makka.greenfarm.service.impl;

import org.makka.greenfarm.service.PowService;
import org.makka.greenfarm.utils.Block;
import org.makka.greenfarm.utils.BlockCache;
import org.makka.greenfarm.utils.CommonUtil;
import org.makka.greenfarm.utils.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 共识机制
 * 采用POW即工作量证明实现共识
 * @author Administrator
 *
 */
@Service
public class PowServiceImpl implements PowService {

    @Autowired
    BlockCache blockCache;

    @Autowired
    BlockServiceImpl blockServiceImpl;

    /**
     * 通过“挖矿”进行工作量证明，实现节点间的共识
     *
     * @return
     */
    public Block mine(){

        // 封装业务数据集合，记录区块产生的节点信息，临时硬编码实现
        List<Transaction> tsaList = new ArrayList<Transaction>();
        Transaction tsa1 = new Transaction();
        tsa1.setId("1");
        tsa1.setBusinessInfo("这是IP为："+ CommonUtil.getLocalIp()+"，端口号为："+blockCache.getP2pport()+"的节点挖矿生成的区块");
        tsaList.add(tsa1);
        Transaction tsa2 = new Transaction();
        tsa2.setId("2");
        tsa2.setBusinessInfo("区块链高度为："+(blockCache.getLatestBlock().getIndex()+1));
        tsaList.add(tsa2);

        // 定义每次哈希函数的结果
        String newBlockHash = "";
        int nonce = 0;
        long start = System.currentTimeMillis();
        System.out.println("开始挖矿");
        while (true) {
            // 计算新区块hash值
            newBlockHash = blockServiceImpl.calculateHash(blockCache.getLatestBlock().getHash(), tsaList, nonce);
            // 校验hash值
            if (blockServiceImpl.isValidHash(newBlockHash)) {
                System.out.println("挖矿完成，正确的hash值：" + newBlockHash);
                System.out.println("挖矿耗费时间：" + (System.currentTimeMillis() - start) + "ms");
                break;
            }
            System.out.println("第"+(nonce+1)+"次尝试计算的hash值：" + newBlockHash);
            nonce++;
        }
        // 创建新的区块
        Block block = blockServiceImpl.createNewBlock(nonce, blockCache.getLatestBlock().getHash(), newBlockHash, tsaList);
        return block;
    }

    /**
     * 验证hash值是否满足系统条件
     * 暂定前4位是0则满足条件
     * @param hash
     * @return
     */
    public boolean isValidHash(String hash) {
        //System.out.println("难度系数："+blockCache.getDifficulty());
        return hash.startsWith("0000");
    }
}
