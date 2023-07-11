package org.makka.greenfarm.controller;


import cn.dev33.satoken.stp.StpUtil;
import org.makka.greenfarm.common.CommonResponse;
import org.makka.greenfarm.domain.Owner;
import org.makka.greenfarm.domain.User;
import org.makka.greenfarm.service.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/owners")
public class OwnerController {
    @Autowired
    private OwnerService ownerService;

    @PostMapping("/login")
    public CommonResponse<String> login(@RequestParam String username, @RequestParam String password) {
        // 验证是否登录成功并返回token
        if (ownerService.validation(username, password)) {
            String oid = ownerService.getOidByOwnername(username);
            StpUtil.login(oid);
            return CommonResponse.creatForSuccess(StpUtil.getTokenValue());
        } else {
            return CommonResponse.creatForError("用户名或密码错误");
        }
    }

    @PostMapping("/register")
    public CommonResponse<String> registerUser(@RequestBody Owner owner) {
        // Return the token to the frontend
        return ownerService.register(owner);
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
            String oid = StpUtil.getLoginIdAsString();
            if (ownerService.updateAvatar(oid, request, file)) {
                return CommonResponse.creatForSuccess("success");
            } else {
                return CommonResponse.creatForError("fail");
            }
        } else {
            return CommonResponse.creatForError("请先登录！");
        }
    }

    @GetMapping("/details")
    public CommonResponse<Owner> getOwnerInfo() {
        if (StpUtil.isLogin()) {
            String oid = StpUtil.getLoginIdAsString();
            return CommonResponse.creatForSuccess(ownerService.getOwnerInfo(oid));
        } else {
            return CommonResponse.creatForError("请先登录！");
        }
    }

    @PostMapping("/details")
    public CommonResponse<Owner> updateOwnerInfo(@RequestBody Owner owner) {
        if (StpUtil.isLogin()) {
            String oid = StpUtil.getLoginIdAsString();
            owner.setOid(oid);
            if (ownerService.updateOwnerInfo(owner)) {
                return CommonResponse.creatForSuccess(owner);
            } else {
                return CommonResponse.creatForError("fail");
            }
        } else {
            return CommonResponse.creatForError("请先登录！");
        }
    }
}

