package org.makka.greenfarm.controller;

import cn.dev33.satoken.stp.StpUtil;
import org.makka.greenfarm.common.CommonResponse;
import org.makka.greenfarm.domain.ReserveProductComment;
import org.makka.greenfarm.service.ReserveProductCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/farms/products")
public class ReserveProductCommentController {

    @Autowired
    private ReserveProductCommentService reserveProductCommentService;

    @PostMapping("/reserve/comments")
    public CommonResponse<List<ReserveProductComment>> postReserveProductComment(@RequestHeader String Authorization,
                                                                       @RequestParam String rpid,
                                                                       @RequestParam String content) {
        // Return the token to the frontend
        if (StpUtil.isLogin()) {
            // 根据uid获取用户信息
            String uid = StpUtil.getLoginIdAsString();
            List<ReserveProductComment> reserveProductComments = reserveProductCommentService.PostReserveProductComment(uid, rpid, content);
            return CommonResponse.creatForSuccess(reserveProductComments);
        } else {
            // 令牌无效或解码错误
            return CommonResponse.creatForError("Authorization is invalid.");
        }

    }


}
