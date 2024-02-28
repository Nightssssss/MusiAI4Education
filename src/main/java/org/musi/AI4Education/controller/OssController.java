package org.musi.AI4Education.controller;

import org.musi.AI4Education.service.OSSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class OssController {

    @Autowired
    private OSSService ossService;

    //上传头像，返回图片的url给
    @PostMapping("/pic/upload")
    public String uploadOssFile(MultipartFile file) throws Exception{
        //获取上传文件 MultipartFile
        //返回图片在oss上的路径
        String url = ossService.uploadFile(file);
        //将URL保存到数据库
        return url;
    }

    @GetMapping("/pic/download")
    public void downloadFile(@RequestParam String qid, @RequestParam String localFilePath) throws IOException {
        String defaultLocalFilePath = "C:\\Users\\lenovo\\Desktop\\"; // 本地保存文件的路径
        ossService.downloadFile(qid,defaultLocalFilePath);
    }
}