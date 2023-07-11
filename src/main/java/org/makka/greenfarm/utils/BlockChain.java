package org.makka.greenfarm.utils;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.Gson;
import io.github.yidasanqian.json.JsonUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.minidev.json.JSONUtil;
import org.apache.commons.collections4.MapUtils;
import org.apache.ibatis.transaction.Transaction;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
public class BlockChain {
    @ApiModelProperty(value = "当前交易列表", dataType = "List<Transaction>")
    @JSONField(serialize = false)
    @JsonIgnore
    private List<Transaction> currentTransactions;

    @ApiModelProperty(value = "所有交易列表", dataType = "List<Transaction>")
    private List<Transaction> transactions;

    @ApiModelProperty(value = "区块列表", dataType = "List<BlockChain>")
    @JSONField(serialize = false)
    @JsonIgnore
    private List<BlockChain> chain;

    @ApiModelProperty(value = "集群的节点列表", dataType = "Set<String>")
    @JSONField(serialize = false)
    @JsonIgnore
    private Set<String> nodes;

    @ApiModelProperty(value = "上一个区块的哈希值", dataType = "String", example = "f461ac428043f328309da7cac33803206cea9912f0d4e8d8cf2786d21e5ff403")
    private String previousHash = "";

    @ApiModelProperty(value = "工作量证明", dataType = "Integer", example = "100")
    private Integer proof = 0;

    @ApiModelProperty(value = "当前区块的索引序号", dataType = "Long", example = "2")
    private Long index = 0L;

    @ApiModelProperty(value = "当前区块的时间戳", dataType = "Long", example = "1526458171000")
    private Long timestamp = 0L;

    @ApiModelProperty(value = "当前区块的哈希值", dataType = "String", example = "g451ac428043f328309da7cac33803206cea9912f0d4e8d8cf2786d21e5ff401")
    private String hash;

    public BlockChain newBlock(Integer proof, String previousHash) {
        BlockChain block = new BlockChain();
        block.index = chain.size() + 1L;
        block.timestamp = System.currentTimeMillis();
        block.transactions.addAll(currentTransactions);
        block.proof = proof;
        block.previousHash = previousHash;
        currentTransactions.clear();
        chain.add(block);
        return block;
    }

    public void newSeedBlock() {
        newBlock(100, "1");
    }

    public String getHash() {
        String json = JsonUtil.toJsonString(this.getCurrentTransactions()) +
                JsonUtil.toJsonString(this.getTransactions()) +
                JsonUtil.toJsonString(this.getChain()) +
                this.getPreviousHash() + this.getProof() + this.getIndex() + this.getTimestamp();
        hash = SHAUtils.getSHA256(json,true);
        return hash;
    }

    public Boolean validProof(Integer lastProof, Integer proof) {
        System.out.println("validProof==>lastProof:" + lastProof + ",proof:" + proof);
        String guessHash = SHAUtils.getSHA256(String.format("{%d}{%d}", lastProof, proof),true);
        return guessHash.startsWith("00");
    }

    public boolean validChain(List<BlockChain> chain) {
        if (CollectionUtils.isEmpty(chain)) {
            return false;
        }

        BlockChain previousBlock = chain.get(0);
        int currentIndex = 1;
        while (currentIndex < chain.size()) {
            BlockChain block = chain.get(currentIndex);
            if (!block.getPreviousHash().equals(previousBlock.getHash())) {
                return false;
            }

            if (!validProof(previousBlock.getProof(), block.getProof())) {
                return false;
            }
            previousBlock = block;
            currentIndex += 1;
        }
        return true;
    }

    public void registerNode(String address) {
        nodes.add(address);
    }

    public boolean resolveConflicts() {
        int maxLength = getChain().size();
        List<BlockChain> newChain = new ArrayList<>();
        for (String node : getNodes()) {
            RestTemplate template = new RestTemplate();
            Map map = template.getForObject(node + "chain", Map.class);
            int length = MapUtils.getInteger(map, "length");
            String json = JsonUtil.toJsonString(MapUtils.getObject(map, "chain"));
            Gson gson = new Gson();
            List<BlockChain> chain = JSONObject.parseObject(json, new TypeReference<List<BlockChain>>() {
            });
            if (length > maxLength && validChain(chain)) {
                maxLength = length;
                newChain = chain;
            }
        }
        if (!CollectionUtils.isEmpty(newChain)) {
            this.chain = newChain;
            return true;
        }
        return false;
    }
}
