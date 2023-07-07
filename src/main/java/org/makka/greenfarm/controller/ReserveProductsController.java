package org.makka.greenfarm.controller;

import org.makka.greenfarm.common.CommonResponse;
import org.makka.greenfarm.domain.ReserveProduct;
import org.makka.greenfarm.service.ReserveProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/farms/products")
public class ReserveProductsController {

    @Autowired
    private ReserveProductService reserveProductService;

    @GetMapping("/reserve/{farmId}")
    public CommonResponse<List<ReserveProduct>> login(@PathVariable("farmId") String farmId) {
        //根据传入的农场编号 获取该农场的 可种植农产品列表
        List<ReserveProduct> reserveProductList = reserveProductService.getReserveProductsByFarmId(farmId);
        if (reserveProductList.size()!=0){
            return CommonResponse.creatForSuccess(reserveProductList);
        }else{
            return CommonResponse.creatForError("该农场可种植农产品列表为空！");
        }
    }

    @GetMapping("/reserve/details/{productId}")
    public CommonResponse<ReserveProduct> getReserveProductDetail(@PathVariable String productId){
        return CommonResponse.creatForSuccess(reserveProductService.getReserveProductDetail(productId));
    }



}
