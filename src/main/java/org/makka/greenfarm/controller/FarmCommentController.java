package org.makka.greenfarm.controller;

import cn.dev33.satoken.stp.StpUtil;
import org.makka.greenfarm.common.CommonResponse;
import org.makka.greenfarm.domain.FarmComment;
import org.makka.greenfarm.service.FarmCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FarmCommentController {
    @Autowired
    private FarmCommentService farmCommentService;

    @PostMapping("/api/farms/comments")
    public CommonResponse<List<FarmComment>> postFarmComment(@RequestParam String farmId, @RequestParam String content) {
        // Return the token to the frontend
        //检查是否登录
        if (StpUtil.isLogin()){
            String uid = StpUtil.getLoginIdAsString();
            List<FarmComment> farmComments = farmCommentService.PostFarmComment(uid, farmId, content);
            return CommonResponse.creatForSuccess(farmComments);
        } else {
            return CommonResponse.creatForError("请先登录！");
        }
    }
}
