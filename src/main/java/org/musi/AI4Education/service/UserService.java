package org.musi.AI4Education.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.musi.AI4Education.common.CommonResponse;
import org.musi.AI4Education.domain.User;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

public interface UserService extends IService<User> {

    public User getUserByUsername(String username);

    //验证用户名和密码是否匹配
    public boolean validation(String username, String password);
    //注册
    public CommonResponse<String> register(User user);

    public String getUidByUsername(String username);

    public boolean updateAvatar(String uid, HttpServletRequest request, MultipartFile file);

    public boolean updateUserInfo(User user);

    public User getUserInfo(String uid);

    public String getVirtualizationByUid(String uid);

    //通过人脸信息连接数据库
    public int addUsersByFace(User user);

    //通过用户名取到用户信息
    public User selectUserByName(String username);

    //通过face_token取到用户信息
    public String getFaceUidByUsername(String username);

    public int getIsPremiumByUid(String uid);

    public void updateUserState(String uid);
}