package org.makka.greenfarm.controller;

import cn.dev33.satoken.stp.StpUtil;
import org.makka.greenfarm.common.CommonResponse;
import org.makka.greenfarm.domain.FarmComment;
import org.makka.greenfarm.domain.ForumComment;
import org.makka.greenfarm.service.ForumCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/forums/comments")
public class ForumCommentController {
    @Autowired
    private ForumCommentService forumCommentService;

    @PostMapping("")
    public CommonResponse<List<ForumComment>> getForumCommentList(@RequestParam String forumId,@RequestParam String content){
        // Return the token to the frontend
        //检查是否登录
        if (StpUtil.isLogin()){
            String uid = StpUtil.getLoginIdAsString();
            List<ForumComment> forumComments = forumCommentService.addForumComment(uid, forumId, content);
            return CommonResponse.creatForSuccess(forumComments);
        } else {
            return CommonResponse.creatForError("请先登录！");
        }
    }


}
