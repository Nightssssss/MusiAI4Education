package org.makka.greenfarm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.makka.greenfarm.common.CommonResponse;
import org.makka.greenfarm.domain.User;
import org.springframework.transaction.annotation.Transactional;
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
}
