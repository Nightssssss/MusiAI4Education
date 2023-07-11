package org.makka.greenfarm.service;

import com.alipay.api.domain.Sale;
import com.baomidou.mybatisplus.extension.service.IService;
import org.makka.greenfarm.domain.ReserveProduct;
import org.makka.greenfarm.domain.SaleProduct;

import java.util.List;

public interface SaleProductService extends IService<SaleProduct> {

    //根据传入的农场编号 获取该农场的 在售农产品列表
    public List<SaleProduct> getSaleProductsByFarmId(String fid);

    public SaleProduct getSaleProductDetail(String productId);

    //新增可种植农产品
    public List<SaleProduct> addSaleProductsBySaleProduct(SaleProduct saleProduct);

    //下架可种植农产品
    public List<SaleProduct> offShelfSaleProductsByProductId(String spid);

    // 推荐
    public List<SaleProduct> getSaleProductRecommendList(String uid);

    // 获取销量前三的农产品
    public List<SaleProduct> getSaleProductTop3List();

    //修改在售农产品
    public List<SaleProduct> updateSaleProductsBySaleProduct(SaleProduct saleProduct);

    //支付订单后更新在售农产品的库存和销量
    public String updateSaleProductsStatusBySpid(String spid, int quantity);
}
