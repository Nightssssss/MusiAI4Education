package org.makka.greenfarm.controller;


import cn.dev33.satoken.stp.StpUtil;
import org.makka.greenfarm.common.CommonResponse;
import org.makka.greenfarm.domain.SaleProductComment;
import org.makka.greenfarm.service.SaleProductCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/farms/products")
public class SaleProductCommentController {

    @Autowired
    private SaleProductCommentService saleProductCommentService;

    @PostMapping("/sale/comments")
    public CommonResponse<List<SaleProductComment>> postSaleProductComment(@RequestParam String spid,
                                                                           @RequestParam String content) {
        if (StpUtil.isLogin()) {
            String uid = StpUtil.getLoginIdAsString();
            // 根据uid获取用户信息
            List<SaleProductComment> saleProductComments = saleProductCommentService.PostSaleProductComment(uid, spid, content);
            return CommonResponse.creatForSuccess(saleProductComments);
        } else {
            // 令牌无效或解码错误
            return CommonResponse.creatForError("请先登录");
        }
    }

}
