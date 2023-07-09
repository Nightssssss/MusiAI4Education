package org.makka.greenfarm.controller;

import cn.dev33.satoken.stp.StpUtil;
import org.makka.greenfarm.common.CommonResponse;
import org.makka.greenfarm.domain.Forum;
import org.makka.greenfarm.service.ForumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/forums")
public class ForumController {
    @Autowired
    private ForumService forumService;

    @GetMapping("/list")
    public CommonResponse<List<Forum>> getForumList() {
        // Return the token to the frontend
        return CommonResponse.creatForSuccess(forumService.getForumList());
    }

    @GetMapping("/details/{forumId}")
    public CommonResponse<Forum> getForumDetail(@PathVariable String forumId){
        Forum forum = forumService.getForumByFid(forumId);
        if(forum==null){
            return CommonResponse.creatForError("该帖子不存在！");
        }else{
            return CommonResponse.creatForSuccess(forum);
        }
    }

    @PostMapping("")
    public CommonResponse<List<Forum>> postForum(@RequestParam String title, @RequestParam String content,
                                                 HttpServletRequest request, @RequestParam MultipartFile file){
        if(StpUtil.isLogin()){
            String uid = StpUtil.getLoginIdAsString();
            if(forumService.addForum(uid, title, content, file, request)){
                // 成功返回所有帖子
                return CommonResponse.creatForSuccess(forumService.getForumList());
            }else{
                return CommonResponse.creatForError("fail");
            }
        } else{
            return CommonResponse.creatForError("请先登录！");
        }
    }
}
