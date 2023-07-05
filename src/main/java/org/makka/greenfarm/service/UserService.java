package org.makka.greenfarm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.makka.greenfarm.domain.User;
import org.springframework.transaction.annotation.Transactional;

public interface UserService extends IService<User> {
    @Transactional
    public User getUserByUsername(String username);
}
