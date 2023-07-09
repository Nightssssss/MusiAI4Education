package org.makka.greenfarm.controller;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import org.makka.greenfarm.common.CommonResponse;
import org.makka.greenfarm.domain.Notice;
import org.makka.greenfarm.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/notices")
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
}
