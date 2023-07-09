package org.makka.greenfarm.controller;

import cn.dev33.satoken.stp.StpUtil;
import org.makka.greenfarm.common.CommonResponse;
import org.makka.greenfarm.domain.ReserveProductFavorite;
import org.makka.greenfarm.domain.SaleProductFavorite;
import org.makka.greenfarm.service.ReserveProductFavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/farms/products")
public class ReserveProductFavoriteController {

    @Autowired
    private ReserveProductFavoriteService reserveProductFavoriteService;

    @PostMapping("/reserve/favorites")
    public CommonResponse<List<ReserveProductFavorite>> addReserveProductFavorite(@RequestParam String rpid) {
        // Return the token to the frontend
        if (StpUtil.isLogin()) {
            String uid = StpUtil.getLoginIdAsString();
            List<ReserveProductFavorite> reserveProductFavorites = reserveProductFavoriteService.ReserveProductFavorite(uid, rpid);
            if(reserveProductFavorites==null){
                return CommonResponse.creatForError("该可种植农产品已经收藏啦");
            }else{
                return CommonResponse.creatForSuccess(reserveProductFavorites);
            }
        } else {
            // 令牌无效或解码错误
            return CommonResponse.creatForError("请先登录");
        }
    }

    @DeleteMapping("/reserve/favorites")
    public CommonResponse<List<ReserveProductFavorite>> cancelReserveProductFavorite(@RequestParam String rpid) {
        // Return the token to the frontend
        if (StpUtil.isLogin()) {
            String uid = StpUtil.getLoginIdAsString();
            List<ReserveProductFavorite> reserveProductFavorites = reserveProductFavoriteService.cancelReserveProductFavorite(uid, rpid);
            //如果返回为空，则代表用户已经没有收藏了
            if(reserveProductFavorites.size()==0){
                return CommonResponse.creatForError("收藏列表已清空！");
            }else{
                return CommonResponse.creatForSuccess(reserveProductFavorites);
            }
        } else {
            // 令牌无效或解码错误
            return CommonResponse.creatForError("请先登录");
        }
    }

    @GetMapping("/reserve/favorites")
    public CommonResponse<List<ReserveProductFavorite>> getReserveProductFavorite() {
        // Return the token to the frontend
        if (StpUtil.isLogin()) {
            String uid = StpUtil.getLoginIdAsString();
            List<ReserveProductFavorite> reserveProductFavorites = reserveProductFavoriteService.getReserveFavoriteList(uid);
            return CommonResponse.creatForSuccess(reserveProductFavorites);
        } else {
            // 令牌无效或解码错误
            return CommonResponse.creatForError("请先登录");
        }
    }

}
