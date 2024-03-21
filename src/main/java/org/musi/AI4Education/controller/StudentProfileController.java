package org.musi.AI4Education.controller;

import cn.dev33.satoken.stp.StpUtil;
import org.musi.AI4Education.common.CommonResponse;
import org.musi.AI4Education.domain.Student;
import org.musi.AI4Education.domain.StudentProfile;
import org.musi.AI4Education.service.StudentProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/student")
public class StudentProfileController {
    @Autowired
    private StudentProfileService studentProfileService;

    @GetMapping("/profile")
    public CommonResponse<List<String>> getStudentTopWrongTypeAndDetails() {
        if (StpUtil.isLogin()) {
            String sid = StpUtil.getLoginIdAsString();
            List<String> result = studentProfileService.getStudentTopWrongTypeAndDetails(sid);
            return CommonResponse.creatForSuccess(result);

        } else {
            return CommonResponse.creatForError("请先登录！");
        }
    }

    @GetMapping("/profile/count")
    public CommonResponse<Map<String, Long>> countQuestionsByDateForStudent() {
        if (StpUtil.isLogin()) {
            Map<String, Long> result = studentProfileService.countQuestionPerDay();
            return CommonResponse.creatForSuccess(result);
        } else {
            return CommonResponse.creatForError("请先登录！");
        }
    }
}
