package org.makka.greenfarm.controller;

import cn.dev33.satoken.stp.StpUtil;
import org.apache.ibatis.annotations.Param;
import org.makka.greenfarm.common.CommonResponse;
import org.makka.greenfarm.domain.ReserveProduct;
import org.makka.greenfarm.service.ReserveProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/farms/products/reserve")
public class ReserveProductController {

    @Autowired
    private ReserveProductService reserveProductService;

    @GetMapping("/{farmId}")
    public CommonResponse<List<ReserveProduct>> login(@PathVariable("farmId") String farmId) {
        //根据传入的农场编号 获取该农场的 可种植农产品列表
        List<ReserveProduct> reserveProductList = reserveProductService.getReserveProductsByFarmId(farmId);
        if (reserveProductList.size() != 0) {
            return CommonResponse.creatForSuccess(reserveProductList);
        } else {
            return CommonResponse.creatForError("该农场可种植农产品列表为空！");
        }
    }

    @GetMapping("/details/{productId}")
    public CommonResponse<ReserveProduct> getReserveProductDetail(@PathVariable String productId) {
        return CommonResponse.creatForSuccess(reserveProductService.getReserveProductDetail(productId));
    }

    @PostMapping("")
    public CommonResponse<List<ReserveProduct>> addReserveProduct(@RequestParam String name, @RequestParam Double uniprice, @RequestParam int stock,
                                                                  @RequestParam String ownerid, @RequestParam int yield, @RequestParam int costTime,
                                                                  @RequestParam String description, @RequestParam MultipartFile image, HttpServletRequest request) {
        //根据传入的农场编号 获取该农场的 可种植农产品列表
        List<ReserveProduct> reserveProductList = reserveProductService.addReserveProductsByReserveProduct(name,ownerid,uniprice,yield,costTime,description,stock,image,request);
//        List<ReserveProduct> reserveProductList = reserveProductService.getReserveProductsByFarmId(reserveProduct.getFid());
        if (reserveProductList.size() != 0) {
            return CommonResponse.creatForSuccess(reserveProductList);
        } else {
            return CommonResponse.creatForError("该农场可种植农产品列表为空！");
        }
    }

    @DeleteMapping("")
    public CommonResponse<List<ReserveProduct>> offShelfReserveProduct(@RequestParam String rpid) {
        //根据传入的农场编号 获取该农场的 可种植农产品列表
        List<ReserveProduct> reserveProductList = reserveProductService.offShelfReserveProductsByProductId(rpid);
        if (reserveProductList.size() != 0) {
            return CommonResponse.creatForSuccess(reserveProductList);
        } else {
            return CommonResponse.creatForError("该农场可种植农产品列表为空！");
        }
    }

    @GetMapping("/list/recommend")
    public CommonResponse<List<ReserveProduct>> getReserveProductFavoriteRecommend() {
        // Return the token to the frontend
        if (StpUtil.isLogin()) {
            String uid = StpUtil.getLoginIdAsString();
            List<ReserveProduct> reserveProductRecommendList = reserveProductService.getReserveProductRecommendList(uid);
            if (reserveProductRecommendList.size() >= 3) {
                return CommonResponse.creatForSuccess(reserveProductRecommendList.subList(0, 3));
            } else {
                return CommonResponse.creatForSuccess(reserveProductService.getReserveProductTop3List());
            }
        } else {
            // 获取销量前三的农产品
            return CommonResponse.creatForSuccess(reserveProductService.getReserveProductTop3List());
        }
    }

    @PutMapping("")
    public CommonResponse<List<ReserveProduct>> updateReserveProduct(@RequestBody ReserveProduct reserveProduct) {
        //根据传入的农场编号 获取该农场的 可种植农产品列表
        List<ReserveProduct> reserveProductList = reserveProductService.updateReserveProductsByReserveProduct(reserveProduct);
        if (reserveProductList.size()!=0){
            return CommonResponse.creatForSuccess(reserveProductList);
        }else{
            return CommonResponse.creatForError("修改的有问题哦!");
        }
    }

}
