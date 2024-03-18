package org.musi.AI4Education.controller;

import cn.dev33.satoken.stp.StpUtil;
import org.musi.AI4Education.common.CommonResponse;
import org.musi.AI4Education.domain.Student;
import org.musi.AI4Education.domain.StudentProfile;
import org.musi.AI4Education.service.StudentProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/student")
public class StudentProfileController {
    @Autowired
    private StudentProfileService studentProfileService;

    @GetMapping("/profile")
    public CommonResponse<HashMap<String,String>> getStudentTopWrongTypeAndDetails(@RequestBody Student student) {
        if (StpUtil.isLogin()) {
            String sid = StpUtil.getLoginIdAsString();
            HashMap<String,String> result = studentProfileService.getStudentTopWrongTypeAndDetails(sid);
            return CommonResponse.creatForSuccess(result);

        } else {
            return CommonResponse.creatForError("请先登录！");
        }
    }
}
