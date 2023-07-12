package org.makka.greenfarm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.makka.greenfarm.domain.Farm;
import org.makka.greenfarm.domain.ReserveProduct;
import org.makka.greenfarm.domain.ReserveProductComment;
import org.makka.greenfarm.domain.User;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ReserveProductService extends IService<ReserveProduct> {

    //农场主获取该农场的 可种植农产品列表
    public List<ReserveProduct> getReserveProducts();

    //根据传入的农场编号 获取该农场的 可种植农产品列表
    public List<ReserveProduct> getReserveProductsByFarmId(String fid);

    public ReserveProduct getReserveProductDetail(String productId);

    //新增可种植农产品
    public List<ReserveProduct> addReserveProductsByReserveProduct(String name, String ownerid, double uniprice, int yield,
                                                                   int costTime, String description, int stock,
                                                                   MultipartFile image, HttpServletRequest request);

    //下架可种植农产品
    public List<ReserveProduct> offShelfReserveProductsByProductId(String rpid);

    //获取可种植农产品推荐
    public List<ReserveProduct> getReserveProductRecommendList(String uid);

    //获取销量前三的农产品
    public List<ReserveProduct> getReserveProductTop3List();

    //修改可种植农产品
    public List<ReserveProduct> updateReserveProductsByReserveProduct(ReserveProduct reserveProduct);

    //支付订单后更新可种植农产品的库存和销量
    public String updateReserveProductsStatusByRpid(String rpid, int quantity);

    public List<ReserveProduct> updateReserveProductImageByRpId(String rpid, String ownerid,MultipartFile image, HttpServletRequest request);
}
