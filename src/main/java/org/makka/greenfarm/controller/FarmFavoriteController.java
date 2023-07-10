package org.makka.greenfarm.controller;


import cn.dev33.satoken.stp.StpUtil;
import org.makka.greenfarm.common.CommonResponse;
import org.makka.greenfarm.domain.Farm;
import org.makka.greenfarm.domain.FarmFavorite;
import org.makka.greenfarm.domain.ReserveProductFavorite;
import org.makka.greenfarm.service.FarmFavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/farms/favorites")
public class FarmFavoriteController {

    @Autowired
    private FarmFavoriteService farmFavoriteService;

    @PostMapping("")
    public CommonResponse<List<FarmFavorite>> addFarmFavorite(@RequestParam String fid) {
        // Return the token to the frontend
        if (StpUtil.isLogin()) {
            String uid = StpUtil.getLoginIdAsString();
            List<FarmFavorite> farmFavorites = farmFavoriteService.addFarmFavorite(uid, fid);
            if(farmFavorites==null){
                return CommonResponse.creatForError("该农场已经收藏啦");
            }else{
                return CommonResponse.creatForSuccess(farmFavorites);
            }
        } else {
            // 令牌无效或解码错误
            return CommonResponse.creatForError("请先登录");
        }
    }


    @DeleteMapping("")
    public CommonResponse<List<FarmFavorite>> cancelFarmFavorite(@RequestParam String ffid) {
        // Return the token to the frontend
        if (StpUtil.isLogin()) {
            String uid = StpUtil.getLoginIdAsString();
            List<FarmFavorite> farmFavorites = farmFavoriteService.cancelFarmFavorite(uid, ffid);
            //如果返回为空，则代表用户已经没有收藏了
            if(farmFavorites.size()==0){
                return CommonResponse.creatForError("收藏列表已清空！");
            }else{
                return CommonResponse.creatForSuccess(farmFavorites);
            }
        } else {
            // 令牌无效或解码错误
            return CommonResponse.creatForError("请先登录");
        }
    }

    @GetMapping("")
    public CommonResponse<List<FarmFavorite>> getFarmFavorite() {
        // Return the token to the frontend
        if (StpUtil.isLogin()) {
            String uid = StpUtil.getLoginIdAsString();
            List<FarmFavorite> farmFavorites = farmFavoriteService.getFarmFavoriteList(uid);
            return CommonResponse.creatForSuccess(farmFavorites);
        } else {
            // 令牌无效或解码错误
            return CommonResponse.creatForError("请先登录");
        }
    }

}