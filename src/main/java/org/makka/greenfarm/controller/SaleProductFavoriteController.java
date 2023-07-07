package org.makka.greenfarm.controller;

import cn.dev33.satoken.stp.StpUtil;
import org.makka.greenfarm.common.CommonResponse;
import org.makka.greenfarm.domain.ReserveProductFavorite;
import org.makka.greenfarm.domain.SaleProductFavorite;
import org.makka.greenfarm.service.ReserveProductFavoriteService;
import org.makka.greenfarm.service.SaleProductFavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/farms/products")
public class SaleProductFavoriteController {

    @Autowired
    private SaleProductFavoriteService saleProductFavoriteService;

    @PostMapping("/sail/favorites")
    public CommonResponse<List<SaleProductFavorite>> postSaleProductFavorite(@RequestParam String spid) {
        // Return the token to the frontend
        if (StpUtil.isLogin()) {
            String uid = StpUtil.getLoginIdAsString();
            List<SaleProductFavorite> saleProductFavorites = saleProductFavoriteService.addSaleProductFavorite(uid, spid);
            //如果返回为空，则代表用户已经为该农产品添加过收藏
            if(saleProductFavorites==null){
                return CommonResponse.creatForError("该在售农产品已经收藏啦");
            }else{
                return CommonResponse.creatForSuccess(saleProductFavorites);
            }
        } else {
            // 令牌无效或解码错误
            return CommonResponse.creatForError("请先登录");
        }
    }

    @DeleteMapping("/sail/favorites")
    public CommonResponse<List<SaleProductFavorite>> cancelSaleProductFavorite(@RequestParam String spid) {
        // Return the token to the frontend
        if (StpUtil.isLogin()) {
            String uid = StpUtil.getLoginIdAsString();
            List<SaleProductFavorite> saleProductFavorites = saleProductFavoriteService.cancelSaleProductFavorite(uid, spid);
            //如果返回为空，则代表用户已经没有收藏了
            if(saleProductFavorites.size()==0){
                return CommonResponse.creatForError("收藏列表已清空！");
            }else{
                return CommonResponse.creatForSuccess(saleProductFavorites);
            }
        } else {
            // 令牌无效或解码错误
            return CommonResponse.creatForError("请先登录");
        }
    }

}
