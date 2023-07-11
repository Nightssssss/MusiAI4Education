package org.makka.greenfarm.service;

import org.makka.greenfarm.BlockChain.Block;
import org.makka.greenfarm.BlockChain.Transaction;

import java.util.List;

public interface BlockService {
    public String createGenesisBlock();

    public Block createNewBlock(int nonce, String previousHash, String hash, List<Transaction> blockTxs);

    public boolean addBlock(Block newBlock);

    public boolean isValidNewBlock(Block newBlock, Block previousBlock);

    public boolean isValidHash(String hash);

    public boolean isValidChain(List<Block> chain);

    public void replaceChain(List<Block> newBlocks);

    public String calculateHash(String previousHash, List<Transaction> currentTransactions, int nonce);

}
