package org.makka.greenfarm.controller;

import cn.dev33.satoken.stp.StpUtil;
import org.makka.greenfarm.common.CommonResponse;
import org.makka.greenfarm.domain.User;
import org.makka.greenfarm.service.UserService;
import org.makka.greenfarm.utils.UploadAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/accounts/login")
    public CommonResponse<String> login(@RequestParam String username, @RequestParam String password) {
        // 验证是否登录成功并返回token
        if (userService.validation(username, password)) {
            String uid = userService.getUidByUsername(username);
            StpUtil.login(uid);
            return CommonResponse.creatForSuccess("success");
        } else {
            return CommonResponse.creatForError("用户名或密码错误");
        }
    }

    @PostMapping("/accounts/upload/avatar")
    public String uploadAvatar(HttpServletRequest request, @RequestParam MultipartFile file) {
        return UploadAction.uploadHandler(request, file) + "";
    }

    @PostMapping("/accounts/register")
    public CommonResponse<String> registerUser(@RequestBody User user, HttpServletRequest request, MultipartFile file) {
        // Return the token to the frontend
        return userService.register(user);
    }

    @PostMapping("/accounts/logout")
    public CommonResponse<String> logout() {
        // Return the token to the frontend
        StpUtil.logout();
        return CommonResponse.creatForSuccess("success");
    }
}

