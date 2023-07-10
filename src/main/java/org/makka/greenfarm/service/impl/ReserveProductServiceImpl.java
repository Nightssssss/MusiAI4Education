package org.makka.greenfarm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.makka.greenfarm.domain.Order;
import org.makka.greenfarm.domain.ReserveProduct;
import org.makka.greenfarm.domain.ReserveProductComment;
import org.makka.greenfarm.domain.ReserveProductFavorite;
import org.makka.greenfarm.mapper.OrderMapper;
import org.makka.greenfarm.mapper.ReserveProductFavoriteMapper;
import org.makka.greenfarm.mapper.ReserveProductMapper;
import org.makka.greenfarm.service.ReserveProductService;
import org.makka.greenfarm.utils.MatrixAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ReserveProductServiceImpl extends ServiceImpl<ReserveProductMapper, ReserveProduct> implements ReserveProductService {

    @Autowired
    private ReserveProductMapper reserveProductMapper;

    @Autowired
    private ReserveProductFavoriteMapper reserveProductFavoriteMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public List<ReserveProduct> getReserveProductsByFarmId(String fid) {
        //使用reserveProductMapper获取对应农场可种植农产品列表
        QueryWrapper<ReserveProduct> wrapper = new QueryWrapper<>();
        wrapper.eq("fid", fid);
        return reserveProductMapper.selectList(wrapper);
    }

    @Override
    public ReserveProduct getReserveProductDetail(String productId) {
        //获取可种植农产品详细信息
        ReserveProduct reserveProduct = reserveProductMapper.selectById(productId);
        //设置评论
        reserveProduct.setReserveProductCommentList(getReserveProductComment(productId));
        return reserveProduct;
    }


    //新增可种植农产品
    @Override
    public List<ReserveProduct> addReserveProductsByReserveProduct(ReserveProduct reserveProduct) {
        reserveProductMapper.insert(reserveProduct);
        return getReserveProductsByFarmId(reserveProduct.getFid());
    }

    @Override
    public List<ReserveProduct> offShelfReserveProductsByProductId(String rpid) {
        QueryWrapper<ReserveProduct> wrapper = new QueryWrapper<>();
        wrapper.eq("rpid", rpid);
        ReserveProduct reserveProduct = reserveProductMapper.selectOne(wrapper);
        reserveProduct.setChoice(0);
        reserveProductMapper.updateById(reserveProduct);
        QueryWrapper<ReserveProduct> wrapper1 = new QueryWrapper<>();
        return reserveProductMapper.selectList(wrapper1);
    }

    public List<ReserveProductComment> getReserveProductComment(String productId) {
        //级联查询评论信息，获取评论人的信息
        List<ReserveProductComment> commentList = reserveProductMapper.getReserveProductComment(productId);
        return commentList;
    }

    public Set<String> recommendByUser(String uid) {
        QueryWrapper<ReserveProductFavorite> queryWrapper = new QueryWrapper<>();
        List<ReserveProductFavorite> reserveProductFavoriteList = reserveProductFavoriteMapper.selectList(queryWrapper);

        QueryWrapper<Order> queryWrapper1 = new QueryWrapper<>();
        List<Order> orderList = orderMapper.selectList(queryWrapper1);

        Map<String, Set<String>> userMapList = new HashMap<>();

        // 获取每个用户收藏的商品放入userMapList
        for (ReserveProductFavorite reserveProductFavorite : reserveProductFavoriteList) {
            String userId = reserveProductFavorite.getUid();
            String reserveProductId = reserveProductFavorite.getRpid();

            // 如果userMapList中没有该用户的收藏商品，则新建一个set
            // 如果有，则直接获取该用户的收藏商品set
            Set<String> reserveProductSet = userMapList.computeIfAbsent(userId, k -> new HashSet<>());
            reserveProductSet.add(reserveProductId);
        }

        // 获取每个用户购买的商品放入userMapList
        for (Order order : orderList) {
            String userId = order.getUid();
            String saleProductId = order.getPid();
            int type = order.getType();
            if (type == 1){
                // 如果userMapList中没有该用户的收藏商品，则新建一个set
                // 如果有，则直接获取该用户的收藏商品set
                Set<String> saleProductSet = userMapList.computeIfAbsent(userId, k -> new HashSet<>());
                saleProductSet.add(saleProductId);
            }
        }

        return MatrixAction.constructMatrix(uid, userMapList);
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

    public List<ReserveProduct> getReserveProductRecommendList(String uid) {
        Set<String> recommendReserveProductList = recommendByUser(uid);
        List<ReserveProduct> reserveProductList = new ArrayList<>();
        for (String rpid : recommendReserveProductList) {
            ReserveProduct reserveProduct = reserveProductMapper.selectById(rpid);
            reserveProductList.add(reserveProduct);
        }
        return reserveProductList;
    }

    public List<ReserveProduct> getReserveProductTop3List(){
        // 获取销量前三的农产品
        QueryWrapper<ReserveProduct> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("sales");
        wrapper.last("limit 3");
        List<ReserveProduct> reserveProductList = reserveProductMapper.selectList(wrapper);
        return reserveProductList;
    }
}
