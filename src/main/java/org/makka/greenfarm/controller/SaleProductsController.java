package org.makka.greenfarm.controller;

import cn.dev33.satoken.stp.StpUtil;
import org.makka.greenfarm.common.CommonResponse;
import org.makka.greenfarm.domain.ReserveProduct;
import org.makka.greenfarm.domain.SaleProduct;
import org.makka.greenfarm.service.SaleProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/farms/products/sale")
public class SaleProductsController {
    @Autowired
    private SaleProductService saleProductService;

    @GetMapping("")
    public CommonResponse<List<SaleProduct>> getOwnerSaleProduct() {
        //根据传入的农场编号 获取该农场的 可种植农产品列表
        List<SaleProduct> saleProductList = saleProductService.getSaleProducts();
        if (saleProductList.size() != 0) {
            return CommonResponse.creatForSuccess(saleProductList);
        } else {
            return CommonResponse.creatForError("尊敬的农场主，该农场在售农产品列表为空！");
        }
    }

    @GetMapping("/{farmId}")
    public CommonResponse<List<SaleProduct>> login(@PathVariable("farmId") String farmId) {

        //根据传入的农场编号 获取该农场的 在售农产品列表
        List<SaleProduct> saleProductList = saleProductService.getSaleProductsByFarmId(farmId);
        if (saleProductList.size()!=0){
            return CommonResponse.creatForSuccess(saleProductList);
        }else{
            return CommonResponse.creatForError("该农场在售农产品列表为空！");
        }
    }

    @GetMapping("/details/{productId}")
    public CommonResponse<SaleProduct> getSaleProductDetail(@PathVariable String productId){
        return CommonResponse.creatForSuccess(saleProductService.getSaleProductDetail(productId));
    }

    @PostMapping("")
    public CommonResponse<List<SaleProduct>> addSaleProduct(@RequestParam String name, @RequestParam Double uniprice, @RequestParam int stock,
                                                            @RequestParam String description, @RequestParam MultipartFile image, HttpServletRequest request) {
        if (StpUtil.isLogin()) {
            String ownerid = StpUtil.getLoginIdAsString();
            //根据传入的农场编号 获取该农场的 可种植农产品列表
            List<SaleProduct> saleProductList = saleProductService.addSaleProductsBySaleProduct(ownerid,name,uniprice,stock,description,image,request);
            if (saleProductList.size()!=0){
                return CommonResponse.creatForSuccess(saleProductList);
            } else {
                return CommonResponse.creatForError("该农场在售农产品列表为空！");
            }
        } else{
            return CommonResponse.creatForError("请先登录！");
        }
    }

    @DeleteMapping("")
    public CommonResponse<List<SaleProduct>> offShelfSaleProduct(@RequestParam String spid) {
        //根据传入的农场编号 获取该农场的 可种植农产品列表
        List<SaleProduct> saleProductList = saleProductService.offShelfSaleProductsByProductId(spid);
        if (saleProductList.size()!=0){
            return CommonResponse.creatForSuccess(saleProductList);
        }else{
            return CommonResponse.creatForError("该农场在售农产品列表为空！");
        }
    }

    @GetMapping("/list/recommend")
    public CommonResponse<List<SaleProduct>> getSaleProductFavoriteRecommend() {
        if (StpUtil.isLogin()) {
            String uid = StpUtil.getLoginIdAsString();
            List<SaleProduct> saleProductRecommendList = saleProductService.getSaleProductRecommendList(uid);
            if (saleProductRecommendList.size()>=3){
                return CommonResponse.creatForSuccess(saleProductRecommendList);
            }else{
                return CommonResponse.creatForSuccess(saleProductService.getSaleProductTop3List());
            }
        } else {
            return CommonResponse.creatForSuccess(saleProductService.getSaleProductTop3List());
        }
    }

    @PutMapping("")
    public CommonResponse<List<SaleProduct>> updateSaleProduct(@RequestBody SaleProduct saleProduct) {
        //根据传入的农场编号 获取该农场的 可种植农产品列表
        List<SaleProduct> saleProductList = saleProductService.updateSaleProductsBySaleProduct(saleProduct);
        if (saleProductList.size()!=0){
            return CommonResponse.creatForSuccess(saleProductList);
        }else{
            return CommonResponse.creatForError("修改的有问题哦!");
        }
    }

    @PutMapping("/image/{spid}")
    public CommonResponse<List<SaleProduct>> updateSaleProductImage(@PathVariable String spid, @RequestParam MultipartFile image, HttpServletRequest request) {
        if (StpUtil.isLogin()) {
            String ownerid = StpUtil.getLoginIdAsString();
            //根据传入的农场编号 获取该农场的 可种植农产品列表
            List<SaleProduct> saleProductList = saleProductService.updateSaleProductImageBySpId(spid,ownerid,image,request);
            if (saleProductList.size()!=0){
                return CommonResponse.creatForSuccess(saleProductList);
            } else {
                return CommonResponse.creatForError("该农场在售农产品列表为空！");
            }
        } else{
            return CommonResponse.creatForError("请先登录！");
        }
    }

}
