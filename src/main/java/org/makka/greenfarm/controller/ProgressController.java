package org.makka.greenfarm.controller;

import cn.dev33.satoken.stp.StpUtil;
import org.makka.greenfarm.common.CommonResponse;
import org.makka.greenfarm.domain.Progress;
import org.makka.greenfarm.service.ProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/progress")
public class ProgressController {
    @Autowired
    private ProgressService progressService;

    @GetMapping("/list")
    public CommonResponse<List<Progress>> getProgressList() {
        // Return the token to the frontend
        //检查是否登录
        if (StpUtil.isLogin()){
            String uid = StpUtil.getLoginIdAsString();
            List<Progress> progressList = progressService.getProgressList(uid);
            return CommonResponse.creatForSuccess(progressList);
        } else {
            return CommonResponse.creatForError("请先登录！");
        }
    }

    @GetMapping("/details/{progressId}")
    public CommonResponse<Progress> getProgressDetail(@PathVariable String progressId){
        if (StpUtil.isLogin()){
            Progress progress = progressService.getProgressByPid(progressId);
            if(progress==null){
                return CommonResponse.creatForError("该进度不存在！");
            }else{
                return CommonResponse.creatForSuccess(progress);
            }
        } else {
            return CommonResponse.creatForError("请先登录！");
        }
    }
}
