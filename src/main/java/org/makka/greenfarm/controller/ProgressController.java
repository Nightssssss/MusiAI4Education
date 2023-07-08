package org.makka.greenfarm.controller;

import cn.dev33.satoken.stp.StpUtil;
import org.makka.greenfarm.service.ProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/progress")
public class ProgressController {
    @Autowired
    private ProgressService progressService;

    @GetMapping("/list")
    public Object getProgressList() {
        // Return the token to the frontend
        if (StpUtil.isLogin()) {
            String uid = StpUtil.getLoginIdAsString();
            return progressService.getProgressList(uid);
        }
        else {
            // 令牌无效或解码错误
            return null;
        }
    }
}
