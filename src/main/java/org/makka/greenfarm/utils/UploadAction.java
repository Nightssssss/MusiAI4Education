package org.makka.greenfarm.utils;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class UploadAction {
    public static Object uploadAvatar(HttpServletRequest request, MultipartFile file) {
        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("fileName", file.getName()); // ⽂件名
        resultMap.put("originalFilename",
                file.getOriginalFilename()); // 原始名称
        resultMap.put("content-type",
                file.getContentType()); // ⽂件类型
        resultMap.put("fileSize", file.getSize() / 1024 +
                "K"); // ⽂件⼤⼩
        try {
// 保存⽂件
            String etc = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
            String serverPath = request.getScheme() +
                    "://" + request.getServerName()
                    + ":" + request.getServerPort() +
                    request.getContextPath() + "/images/avatar/";
            String fileName = UUID.randomUUID() + "." + etc;
            resultMap.put("filePath", serverPath + fileName); // ⽂件地址(服务器访问地址)
            System.out.println("filePath: " + resultMap.get("filePath"));
// ⽂件保存再真实路径下
            File saveFile = new File(request.getServletContext().getRealPath("/images/avatar") + fileName);
            if (!saveFile.getParentFile().exists()) { // ⽬录不存在，创建⽬录
                saveFile.mkdirs();
            }
            file.transferTo(saveFile); // 保存上传⽂件
        } catch (IOException e) {
            System.err.println("error-path: /upload/file, message: " + e.getMessage());
        }
        return resultMap.get("filePath");
    }
}
