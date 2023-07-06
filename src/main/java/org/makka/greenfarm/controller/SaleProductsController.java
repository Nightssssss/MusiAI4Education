package org.makka.greenfarm.controller;

import org.makka.greenfarm.common.CommonResponse;
import org.makka.greenfarm.domain.ReserveProduct;
import org.makka.greenfarm.domain.SaleProduct;
import org.makka.greenfarm.service.SaleProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/farms/products")
public class SaleProductsController {
    @Autowired
    private SaleProductService saleProductService;

    @GetMapping("/sale/{farmId}")
    public CommonResponse<List<SaleProduct>> login(@PathVariable("farmId") String farmId) {

        //根据传入的农场编号 获取该农场的 在售农产品列表
        List<SaleProduct> saleProductList = saleProductService.getSaleProductsByFarmId(farmId);
        if (saleProductList.size()!=0){
            return CommonResponse.creatForSuccess(saleProductList);
        }else{
            return CommonResponse.creatForError("该农场在售农产品列表为空！");
        }
    }

    @GetMapping("/sale/details/{productId}")
    public CommonResponse<SaleProduct> getSaleProductDetail(@PathVariable String productId){
        System.out.println("this is saleProduct!");
        // Return the token to the frontend
        return CommonResponse.creatForSuccess(saleProductService.getSaleProductDetail(productId));
    }


}
