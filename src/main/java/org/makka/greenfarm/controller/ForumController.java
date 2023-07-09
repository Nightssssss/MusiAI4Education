package org.makka.greenfarm.controller;

import org.makka.greenfarm.common.CommonResponse;
import org.makka.greenfarm.domain.Forum;
import org.makka.greenfarm.service.ForumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

//    @PostMapping
}
