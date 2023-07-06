package org.makka.greenfarm.controller;

import org.makka.greenfarm.common.CommonResponse;
import org.makka.greenfarm.domain.FarmComment;
import org.makka.greenfarm.service.FarmCommentService;
import org.makka.greenfarm.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FarmCommentController {
    @Autowired
    private FarmCommentService farmCommentService;

    @PostMapping("/api/farms/comments")
    public CommonResponse<List<FarmComment>> postFarmComment(@RequestHeader String Authorization,
                                                       @RequestParam String farmId,
                                                       @RequestParam String content) {
        // Return the token to the frontend
        if (Authorization == null || Authorization.length() == 0) {
            return CommonResponse.creatForError("Authorization is required.");
        } else {
            // 提取uid
            String uid = JwtUtil.extractUid(Authorization);
            System.out.println(uid);
            if (uid != null) {
                // 根据uid获取用户信息
                List<FarmComment> farmComments = farmCommentService.PostFarmComment(uid, farmId, content);
                return CommonResponse.creatForSuccess(farmComments);
            } else {
                // 令牌无效或解码错误
                return CommonResponse.creatForError("Authorization is invalid.");
            }
        }
    }
}
