package org.makka.greenfarm.controller;

import org.makka.greenfarm.common.CommonResponse;
import org.makka.greenfarm.domain.ReserveProductComment;
import org.makka.greenfarm.service.ReserveProductCommentService;
import org.makka.greenfarm.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/farms/products")
public class ReserveProductFavoriteController {

    @Autowired
    private ReserveProductCommentService reserveProductCommentService;

    @PostMapping("/reserve/favorites")
    public CommonResponse<List<ReserveProductComment>> postReserveProductComment(@RequestHeader String Authorization,
                                                                                 @RequestParam String rpid) {
        // Return the token to the frontend
        if (Authorization == null || Authorization.length() == 0) {
            return CommonResponse.creatForError("Authorization is required.");
        } else {
            // 提取uid
            String uid = JwtUtil.extractUid(Authorization);
            if (uid != null) {
                // 根据uid获取用户信息
                List<ReserveProductComment> reserveProductComments = reserveProductCommentService.PostReserveProductComment(uid, rpid, content);
                return CommonResponse.creatForSuccess(reserveProductComments);
            } else {
                // 令牌无效或解码错误
                return CommonResponse.creatForError("Authorization is invalid.");
            }
        }
    }
}
