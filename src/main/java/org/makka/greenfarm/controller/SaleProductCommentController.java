package org.makka.greenfarm.controller;


import org.makka.greenfarm.common.CommonResponse;
import org.makka.greenfarm.domain.ReserveProductComment;
import org.makka.greenfarm.domain.SaleProductComment;
import org.makka.greenfarm.service.SaleProductCommentService;
import org.makka.greenfarm.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/farms/products")
public class SaleProductCommentController {

    @Autowired
    private SaleProductCommentService saleProductCommentService;

    @PostMapping("/sale/comments")
    public CommonResponse<List<SaleProductComment>> postSaleProductComment(@RequestHeader String Authorization,
                                                                           @RequestParam String spid,
                                                                           @RequestParam String content) {
        // Return the token to the frontend
        if (Authorization == null || Authorization.length() == 0) {
            return CommonResponse.creatForError("Authorization is required.");
        } else {
            // 提取uid
            String uid = JwtUtil.extractUid(Authorization);
            if (uid != null) {
                // 根据uid获取用户信息
                List<SaleProductComment> saleProductComments = saleProductCommentService.PostSaleProductComment(uid, spid, content);
                return CommonResponse.creatForSuccess(saleProductComments);
            } else {
                // 令牌无效或解码错误
                return CommonResponse.creatForError("Authorization is invalid.");
            }
        }
    }


}
