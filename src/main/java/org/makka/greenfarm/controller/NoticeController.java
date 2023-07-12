package org.makka.greenfarm.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import org.makka.greenfarm.common.CommonResponse;
import org.makka.greenfarm.domain.Notice;
import org.makka.greenfarm.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/notices")
public class NoticeController {
    @Autowired
    private NoticeService noticeService;

    @GetMapping("/list")
    public CommonResponse<List<Notice>> getNoticeList() {
        // Return the token to the frontend
        return CommonResponse.creatForSuccess(noticeService.getNoticeList());
    }

    @GetMapping("/details/{noticeId}")
    public CommonResponse<Notice> getNoticeDetail(@PathVariable String noticeId){
        Notice notice = noticeService.getNoticeByNid(noticeId);
        if(notice==null){
            return CommonResponse.creatForError("该公告不存在！");
        }else{
            return CommonResponse.creatForSuccess(notice);
        }
    }

    @GetMapping("/mine")
    // 获取当前用户的所有公告
    public CommonResponse<List<Notice>> getNoticeListByOwnerId(){
        if(StpUtil.isLogin()){
            String ownerid = StpUtil.getLoginIdAsString();
            return CommonResponse.creatForSuccess(noticeService.getNoticeListByOwnerId(ownerid));
        } else{
            return CommonResponse.creatForError("请先登录！");
        }
    }

    @PostMapping("")
    public CommonResponse<List<Notice>> addNotice(@RequestParam String title, @RequestParam String content,
                                                  HttpServletRequest request, @RequestParam MultipartFile image) {
        if (StpUtil.isLogin()) {
            String ownerid = StpUtil.getLoginIdAsString();
            if (noticeService.addNotice(ownerid, title, content, image, request)) {
                // 成功返回所有公告
                return CommonResponse.creatForSuccess(noticeService.getNoticeListByOwnerId(ownerid));
            } else {
                return CommonResponse.creatForError("fail");
            }
        } else {
            return CommonResponse.creatForError("请先登录！");
        }
    }

    @PutMapping("/{noticeId}")
    public CommonResponse<List<Notice>> updateNotice(@PathVariable String noticeId, @RequestParam String title, @RequestParam String content) {
        // Return the token to the frontend
        if (StpUtil.isLogin()) {
            String ownerid = StpUtil.getLoginIdAsString();
            if (noticeService.updateNotice(noticeId, title, content)) {
                // 成功返回所有公告
                return CommonResponse.creatForSuccess(noticeService.getNoticeListByOwnerId(ownerid));
            } else {
                return CommonResponse.creatForError("fail");
            }
        } else {
            return CommonResponse.creatForError("请先登录！");
        }
    }

    @PutMapping("/image/{noticeId}")
    public CommonResponse<List<Notice>> updateNoticeImage(@PathVariable String noticeId, HttpServletRequest request, @RequestParam MultipartFile image) {
        // Return the token to the frontend
        if (StpUtil.isLogin()) {
            String ownerid = StpUtil.getLoginIdAsString();
            if (noticeService.updateNoticeImage(noticeId, image, request)) {
                // 成功返回所有公告
                return CommonResponse.creatForSuccess(noticeService.getNoticeListByOwnerId(ownerid));
            } else {
                return CommonResponse.creatForError("fail");
            }
        } else {
            return CommonResponse.creatForError("请先登录！");
        }
    }

    @DeleteMapping("/{noticeId}")
    public CommonResponse<List<Notice>> deleteNoticeByNid(@PathVariable String nid) {
        // Return the token to the frontend
        if (StpUtil.isLogin()) {
            String ownerid = StpUtil.getLoginIdAsString();
            if (noticeService.deleteNoticeById(nid)) {
                // 成功返回所有公告
                return CommonResponse.creatForSuccess(noticeService.getNoticeListByOwnerId(ownerid));
            } else {
                return CommonResponse.creatForError("fail");
            }
        } else {
            return CommonResponse.creatForError("请先登录！");
        }
    }
}
