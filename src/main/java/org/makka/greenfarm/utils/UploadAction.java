package org.makka.greenfarm.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class UploadAction {
    @Value("${file.savePath}")
    private static String fileSavePath;

    public static String uploadAvatar(HttpServletRequest request, MultipartFile file) {
        System.out.println("图片上传，保存位置：" + fileSavePath);
        //3.给文件重新设置一个名字
        //后缀
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        String newFileName= UUID.randomUUID().toString().replaceAll("-", "")+suffix;
        //4.创建这个新文件
        File newFile = new File(fileSavePath + "/user/avatar/" + newFileName);
        if (!newFile.exists()) {
            newFile.mkdirs();
        }
        //5.复制操作
        try {
            file.transferTo(newFile);
            //协议 :// ip地址 ：端口号 / 文件目录(/images/2020/03/15/xxx.jpg)
            String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/images/user/avatar/"  + newFileName;
            System.out.println("图片上传，访问URL：" + url);
            return url;
        } catch (IOException e) {
            return null;
        }
    }

    public static String uploadForumImage(HttpServletRequest request, MultipartFile file) {
        File dir = new File(fileSavePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        System.out.println("图片上传，保存位置：" + fileSavePath);
        //3.给文件重新设置一个名字
        //后缀
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        String newFileName= UUID.randomUUID().toString().replaceAll("-", "")+suffix;
        //4.创建这个新文件
        File newFile = new File(fileSavePath + "/forum/" + newFileName);
        //5.复制操作
        try {
            file.transferTo(newFile);
            //协议 :// ip地址 ：端口号 / 文件目录(/images/2020/03/15/xxx.jpg)
            String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/images/forum/"  + newFileName;
            System.out.println("图片上传，访问URL：" + url);
            return url;
        } catch (IOException e) {
            return null;
        }
    }

    public static String uploadNoticeImage(HttpServletRequest request, MultipartFile file) {
        File dir = new File(fileSavePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        System.out.println("图片上传，保存位置：" + fileSavePath);
        //3.给文件重新设置一个名字
        //后缀
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        String newFileName= UUID.randomUUID().toString().replaceAll("-", "")+suffix;
        //4.创建这个新文件
        File newFile = new File(fileSavePath + "/notice/" + newFileName);
        //5.复制操作
        try {
            file.transferTo(newFile);
            //协议 :// ip地址 ：端口号 / 文件目录(/images/2020/03/15/xxx.jpg)
            String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/images/notice/"  + newFileName;
            System.out.println("图片上传，访问URL：" + url);
            return url;
        } catch (IOException e) {
            return null;
        }
    }
}
