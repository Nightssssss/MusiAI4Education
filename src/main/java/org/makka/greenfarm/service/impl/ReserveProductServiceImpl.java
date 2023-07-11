package org.makka.greenfarm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.makka.greenfarm.domain.*;
import org.makka.greenfarm.mapper.FarmMapper;
import org.makka.greenfarm.mapper.OrderMapper;
import org.makka.greenfarm.mapper.ReserveProductFavoriteMapper;
import org.makka.greenfarm.mapper.ReserveProductMapper;
import org.makka.greenfarm.service.ReserveProductService;
import org.makka.greenfarm.utils.MatrixAction;
import org.makka.greenfarm.utils.UploadAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.time.Year;
import java.util.*;

@Service
public class ReserveProductServiceImpl extends ServiceImpl<ReserveProductMapper, ReserveProduct> implements ReserveProductService {

    @Autowired
    private ReserveProductMapper reserveProductMapper;

    @Autowired
    private ReserveProductFavoriteMapper reserveProductFavoriteMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private FarmMapper farmMapper;

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
    public List<ReserveProduct> addReserveProductsByReserveProduct(String name, String ownerid,  double uniprice, int yield,
                                                                   int costTime,  String description,  int stock,
                                                                    MultipartFile image, HttpServletRequest request) {

        QueryWrapper<Farm> wrapper = new QueryWrapper<>();
        wrapper.eq("ownerid", ownerid);
        Farm farm = farmMapper.selectOne(wrapper);
        String fid = farm.getFid();

        ReserveProduct reserveProduct = new ReserveProduct();
        reserveProduct.setRpid(String.valueOf(System.currentTimeMillis()));
        reserveProduct.setName(name);
        reserveProduct.setFid(fid);
        reserveProduct.setUniprice(uniprice);

        reserveProduct.setCostTime(costTime);
        reserveProduct.setYield(yield);

        reserveProduct.setStock(stock);
        reserveProduct.setDescription(description);
        reserveProduct.setChoice(1);
        String imageUrl = UploadAction.uploadSaleProductImage(request, image);
        reserveProduct.setPicture(imageUrl);

        reserveProductMapper.insert(reserveProduct);
        return getReserveProductsByFarmId(fid);

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

    @Cacheable(value = "reserveProducts", key = "4")
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

    @Override
    public List<ReserveProduct> updateReserveProductsByReserveProduct(ReserveProduct reserveProduct) {
        QueryWrapper<ReserveProduct> wrapper = new QueryWrapper<>();
        wrapper.eq("rpid", reserveProduct.getRpid());
        ReserveProduct reserveProduct1 = reserveProductMapper.selectOne(wrapper);

        reserveProduct1.setName(reserveProduct.getName());
        reserveProduct1.setUniprice(reserveProduct.getUniprice());
        reserveProduct1.setYield(reserveProduct.getYield());
        reserveProduct1.setCostTime(reserveProduct.getCostTime());
        reserveProduct1.setStock(reserveProduct.getStock());
        reserveProduct1.setDescription(reserveProduct.getDescription());

        //修改商品图片

        //数据库中更新
        reserveProductMapper.updateById(reserveProduct1);

        QueryWrapper<ReserveProduct> wrapper1 = new QueryWrapper<>();
        return reserveProductMapper.selectList(wrapper1);
    }

    @Override
    public String updateReserveProductsStatusByRpid(String rpid, int quantity) {
        QueryWrapper<ReserveProduct> wrapper = new QueryWrapper<>();
        wrapper.eq("rpid", rpid);
        ReserveProduct reserveProduct = reserveProductMapper.selectOne(wrapper);
        // 更新可种植农产品库存
        reserveProduct.setStock(reserveProduct.getStock()-quantity);
        // 更新可种植农产品销量
        reserveProduct.setSales(reserveProduct.getSales()+quantity);
        reserveProductMapper.updateById(reserveProduct);
        return "success";
    }
}
