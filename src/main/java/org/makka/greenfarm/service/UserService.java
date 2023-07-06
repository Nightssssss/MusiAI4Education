package org.makka.greenfarm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.makka.greenfarm.common.CommonResponse;
import org.makka.greenfarm.domain.User;
import org.springframework.transaction.annotation.Transactional;

public interface UserService extends IService<User> {

    public User getUserByUsername(String username);

    //验证用户名和密码是否匹配
    public boolean validation(String username, String password);
    //注册
    public CommonResponse<String> register(User user);

    public String getUidByUsername(String username);
}
