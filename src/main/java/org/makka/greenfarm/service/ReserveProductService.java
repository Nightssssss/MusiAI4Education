package org.makka.greenfarm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.makka.greenfarm.domain.ReserveProduct;
import org.makka.greenfarm.domain.User;

import java.util.List;

public interface ReserveProductService extends IService<ReserveProduct> {

    //根据传入的农场编号 获取该农场的 可种植农产品列表
    public List<ReserveProduct> getReserveProductsByFarmId(String fid);

}
