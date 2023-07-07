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
@RequestMapping("/api/accounts")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
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

    @PostMapping("/register")
    public CommonResponse<String> registerUser(@RequestBody User user) {
        // Return the token to the frontend
        return userService.register(user);
    }

    @PostMapping("/logout")
    public CommonResponse<String> logout() {
        // Return the token to the frontend
        StpUtil.logout();
        return CommonResponse.creatForSuccess("success");
    }

    @PostMapping("/avatar")
    public CommonResponse<String> uploadAvatar(HttpServletRequest request, @RequestParam MultipartFile file) {
        if (StpUtil.isLogin()) {
            String uid = StpUtil.getLoginIdAsString();
            if (userService.updateAvatar(uid, request, file)) {
                return CommonResponse.creatForSuccess("success");
            } else {
                return CommonResponse.creatForError("fail");
            }
        } else {
            return CommonResponse.creatForError("请先登录！");
        }
    }

    @GetMapping("/details")
    public CommonResponse<User> getUserInfo() {
        if (StpUtil.isLogin()) {
            String uid = StpUtil.getLoginIdAsString();
            return CommonResponse.creatForSuccess(userService.getUserInfo(uid));
        } else {
            return CommonResponse.creatForError("请先登录！");
        }
    }

    @PostMapping("/details")
    public CommonResponse<User> updateUserInfo(@RequestBody User user) {
        if (StpUtil.isLogin()) {
            String uid = StpUtil.getLoginIdAsString();
            user.setUid(uid);
            if (userService.updateUserInfo(user)) {
                return CommonResponse.creatForSuccess(user);
            } else {
                return CommonResponse.creatForError("fail");
            }
        } else {
            return CommonResponse.creatForError("请先登录！");
        }
    }
}

