package org.makka.greenfarm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.makka.greenfarm.domain.*;
import org.makka.greenfarm.mapper.*;
import org.makka.greenfarm.mapper.SaleProductMapper;
import org.makka.greenfarm.service.SaleProductService;
import org.makka.greenfarm.utils.MatrixAction;
import org.makka.greenfarm.utils.UploadAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class SaleProductServiceImpl extends ServiceImpl<SaleProductMapper, SaleProduct> implements SaleProductService {
    @Autowired
    private SaleProductMapper saleProductMapper;

    @Autowired
    private SaleProductFavoriteMapper saleProductFavoriteMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private FarmMapper farmMapper;

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
    public List<SaleProduct> addSaleProductsBySaleProduct(String ownerid,String name,double uniprice,int stock,String description,
                                                          MultipartFile image, HttpServletRequest request) {
        QueryWrapper<Farm> wrapper = new QueryWrapper<>();
        wrapper.eq("ownerid", ownerid);
        Farm farm = farmMapper.selectOne(wrapper);
        String fid = farm.getFid();

        SaleProduct saleProduct = new SaleProduct();
        saleProduct.setSpid(String.valueOf(System.currentTimeMillis()));
        saleProduct.setName(name);
        saleProduct.setFid(fid);
        saleProduct.setUniprice(uniprice);
        saleProduct.setStock(stock);
        saleProduct.setDescription(description);
        saleProduct.setShelves(1);
        String imageUrl = UploadAction.uploadSaleProductImage(request, image);
        saleProduct.setPicture(imageUrl);

        saleProductMapper.insert(saleProduct);
        return getSaleProductsByFarmId(fid);
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
        QueryWrapper<SaleProductFavorite> queryWrapper = new QueryWrapper<>();
        List<SaleProductFavorite> saleProductFavoriteList = saleProductFavoriteMapper.selectList(queryWrapper);

        QueryWrapper<Order> queryWrapper1 = new QueryWrapper<>();
        List<Order> orderList = orderMapper.selectList(queryWrapper1);

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

        // 获取每个用户购买的商品放入userMapList
        for (Order order : orderList) {
            String userId = order.getUid();
            String saleProductId = order.getPid();
            int type = order.getType();
            if (type == 0){
                // 如果userMapList中没有该用户的收藏商品，则新建一个set
                // 如果有，则直接获取该用户的收藏商品set
                Set<String> saleProductSet = userMapList.computeIfAbsent(userId, k -> new HashSet<>());
                saleProductSet.add(saleProductId);
            }
        }

        return MatrixAction.constructMatrix(uid, userMapList);
    }

    @Cacheable(value = "saleProducts", key = "5")
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

    @Override
    public List<SaleProduct> updateSaleProductsBySaleProduct(SaleProduct saleProduct) {
        QueryWrapper<SaleProduct> wrapper = new QueryWrapper<>();
        wrapper.eq("spid", saleProduct.getSpid());
        SaleProduct saleProduct1 = saleProductMapper.selectOne(wrapper);

        saleProduct1.setName(saleProduct.getName());
        saleProduct1.setUniprice(saleProduct.getUniprice());
        saleProduct1.setStock(saleProduct.getStock());
        saleProduct1.setDescription(saleProduct.getDescription());
        saleProduct1.setShelves(saleProduct.getShelves());
        //saleProduct1.setPicture(saleProduct.getPicture());

        //修改商品图片

        //数据库中更新
        saleProductMapper.updateById(saleProduct1);
        QueryWrapper<SaleProduct> wrapper1 = new QueryWrapper<>();
        return saleProductMapper.selectList(wrapper1);
    }

    @Override
    public String updateSaleProductsStatusBySpid(String spid, int quantity) {
        QueryWrapper<SaleProduct> wrapper = new QueryWrapper<>();
        wrapper.eq("spid", spid);
        SaleProduct saleProduct = saleProductMapper.selectOne(wrapper);
        // 更新可种植农产品库存
        saleProduct.setStock(saleProduct.getStock()-quantity);
        // 更新可种植农产品销量
        saleProduct.setSales(saleProduct.getSales()+quantity);
        saleProductMapper.updateById(saleProduct);
        return "success";
    }

    @Override
    public List<SaleProduct> updateSaleProductImageBySpId(String spid,String ownerid, MultipartFile image, HttpServletRequest request) {
        QueryWrapper<Farm> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("ownerid", ownerid);
        Farm farm = farmMapper.selectOne(wrapper1);
        String fid = farm.getFid();

        SaleProduct saleProduct = saleProductMapper.selectById(spid);
        String imageUrl = UploadAction.uploadSaleProductImage(request, image);
        saleProduct.setPicture(imageUrl);
        saleProductMapper.updateById(saleProduct);
        QueryWrapper<SaleProduct> wrapper = new QueryWrapper<>();
        wrapper.eq("fid", fid);
        return saleProductMapper.selectList(wrapper);
    }
}
