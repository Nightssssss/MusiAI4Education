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
@RequestMapping("/forums")
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
                                                 HttpServletRequest request, @RequestParam MultipartFile image){
        if(StpUtil.isLogin()){
            String uid = StpUtil.getLoginIdAsString();
            if(forumService.addForum(uid, title, content, image, request)){
                // 成功返回所有帖子
                return CommonResponse.creatForSuccess(forumService.getForumList());
            }else{
                return CommonResponse.creatForError("fail");
            }
        } else{
            return CommonResponse.creatForError("请先登录！");
        }
    }

    @GetMapping("/mine")
    // 获取当前用户的所有帖子
    public CommonResponse<List<Forum>> getForumListByUid(){
        if(StpUtil.isLogin()){
            String uid = StpUtil.getLoginIdAsString();
            return CommonResponse.creatForSuccess(forumService.getForumListByUid(uid));
        } else{
            return CommonResponse.creatForError("请先登录！");
        }
    }

    @PutMapping("/{forumId}")
    // 修改帖子
    public CommonResponse<List<Forum>> updateForum(@PathVariable String forumId, @RequestParam String title, @RequestParam String content,
                                                   HttpServletRequest request, @RequestParam MultipartFile image){
        if(StpUtil.isLogin()){
            String uid = StpUtil.getLoginIdAsString();
            if(forumService.updateForum(forumId, title, content, image, request)){
                // 成功返回所有帖子
                return CommonResponse.creatForSuccess(forumService.getForumListByUid(uid));
            }else{
                return CommonResponse.creatForError("fail");
            }
        } else{
            return CommonResponse.creatForError("请先登录！");
        }
    }

    @DeleteMapping("/{forumId}")
    // 删除帖子
    public CommonResponse<List<Forum>> deleteForum(@PathVariable String forumId){
        if(StpUtil.isLogin()){
            String uid = StpUtil.getLoginIdAsString();
            if(forumService.deleteForum(forumId)){
                // 成功返回所有帖子
                return CommonResponse.creatForSuccess(forumService.getForumListByUid(uid));
            }else{
                return CommonResponse.creatForError("fail");
            }
        } else{
            return CommonResponse.creatForError("请先登录！");
        }
    }
}
