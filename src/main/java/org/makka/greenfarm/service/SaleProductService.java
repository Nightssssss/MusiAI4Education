package org.makka.greenfarm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.makka.greenfarm.domain.ReserveProduct;
import org.makka.greenfarm.domain.SaleProduct;

import java.util.List;

public interface SaleProductService extends IService<SaleProduct> {

    //根据传入的农场编号 获取该农场的 在售农产品列表
    public List<SaleProduct> getSaleProductsByFarmId(String fid);

    public SaleProduct getSaleProductDetail(String productId);
}
