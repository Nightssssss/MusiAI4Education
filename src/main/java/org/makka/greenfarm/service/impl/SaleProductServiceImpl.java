package org.makka.greenfarm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.makka.greenfarm.domain.*;
import org.makka.greenfarm.mapper.SaleProductMapper;
import org.makka.greenfarm.mapper.SaleProductFavoriteMapper;
import org.makka.greenfarm.mapper.SaleProductMapper;
import org.makka.greenfarm.service.SaleProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SaleProductServiceImpl extends ServiceImpl<SaleProductMapper, SaleProduct> implements SaleProductService {
    @Autowired
    private SaleProductMapper saleProductMapper;

    @Autowired
    private SaleProductFavoriteMapper saleProductFavoriteMapper;

    @Override
    public List<SaleProduct> getSaleProductsByFarmId(String fid) {
        //使用saleProductMapper获取对应农场可种植农产品列表
        QueryWrapper<SaleProduct> wrapper = new QueryWrapper<>();
        wrapper.eq("fid", fid);
        return saleProductMapper.selectList(wrapper);
    }

    @Override
    public SaleProduct getSaleProductDetail(String productId) {
        //获取可种植农产品详细信息
        SaleProduct saleProduct = saleProductMapper.selectById(productId);
        System.out.println("the sale porduct is"+saleProduct);
        //设置评论
        saleProduct.setSaleProductCommentList(getSaleProductComment(productId));
        System.out.println("the sale porduct is"+saleProduct);
        return saleProduct;
    }

    @Override
    public List<SaleProduct> addSaleProductsBySaleProduct(SaleProduct saleProduct) {
        saleProductMapper.insert(saleProduct);
        return getSaleProductsByFarmId(saleProduct.getFid());
    }

    @Override
    public List<SaleProduct> offShelfSaleProductsByProductId(String spid) {
        QueryWrapper<SaleProduct> wrapper = new QueryWrapper<>();
        wrapper.eq("spid", spid);
        SaleProduct saleProduct = saleProductMapper.selectOne(wrapper);
        saleProduct.setShelves(0);
        saleProductMapper.updateById(saleProduct);
        QueryWrapper<SaleProduct> wrapper1 = new QueryWrapper<>();
        return saleProductMapper.selectList(wrapper1);
    }

    public List<SaleProductComment> getSaleProductComment(String productId) {
        //级联查询评论信息，获取评论人的信息
        List<SaleProductComment> commentList = saleProductMapper.getSaleProductComment(productId);
        return commentList;
    }

    public Set<String> recommendByUser(String uid) {
        Set<String> recommendSaleProductList = new HashSet<>();

        QueryWrapper<SaleProductFavorite> queryWrapper = new QueryWrapper<>();
        List<SaleProductFavorite> saleProductFavoriteList = saleProductFavoriteMapper.selectList(queryWrapper);

        Map<String, Set<String>> userMapList = new HashMap<>();

        // 获取每个用户收藏的商品放入userMapList
        for (SaleProductFavorite saleProductFavorite : saleProductFavoriteList) {
            String userId = saleProductFavorite.getUid();
            String saleProductId = saleProductFavorite.getSpid();

            // 如果userMapList中没有该用户的收藏商品，则新建一个set
            // 如果有，则直接获取该用户的收藏商品set
            Set<String> saleProductSet = userMapList.computeIfAbsent(userId, k -> new HashSet<>());
            saleProductSet.add(saleProductId);
        }

        // 构造相似度矩阵
        Set<String> currentUserSaleProducts = userMapList.get(uid);
        for (Map.Entry<String, Set<String>> entry : userMapList.entrySet()) {
            String otherUserId = entry.getKey();
            if (!otherUserId.equals(uid)) {
                Set<String> otherUserSaleProducts = entry.getValue();
                double similarity = computeSimilarity(currentUserSaleProducts, otherUserSaleProducts);
                if (similarity > 0) {
                    recommendSaleProductList.addAll(otherUserSaleProducts);
                }
            }
        }
        // 去掉原本就收藏的商品
        recommendSaleProductList.removeAll(currentUserSaleProducts);

        return recommendSaleProductList;
    }

    // 计算两个用户的相似度
    public double computeSimilarity(Set<String> set1, Set<String> set2) {
        int commonCount = 0;
        for (String item : set1) {
            if (set2.contains(item)) {
                commonCount++;
            }
        }
        double similarity = commonCount / Math.sqrt(set1.size() * set2.size());
        return similarity;
    }

    public List<SaleProduct> getSaleProductRecommendList(String uid) {
        Set<String> recommendSaleProductList = recommendByUser(uid);
        List<SaleProduct> saleProductList = new ArrayList<>();
        for (String rpid : recommendSaleProductList) {
            SaleProduct saleProduct = saleProductMapper.selectById(rpid);
            saleProductList.add(saleProduct);
        }
        return saleProductList;
    }

    public List<SaleProduct> getSaleProductTop3List() {
        // 获取销量前三的商品
        QueryWrapper<SaleProduct> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("sales");
        queryWrapper.last("limit 3");
        List<SaleProduct> saleProductList = saleProductMapper.selectList(queryWrapper);
        return saleProductList;
    }

}
