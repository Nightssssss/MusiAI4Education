package org.makka.greenfarm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.junit.Test;
import org.makka.greenfarm.common.CommonResponse;
import org.makka.greenfarm.domain.User;
import org.makka.greenfarm.mapper.UserMapper;
import org.makka.greenfarm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private UserMapper userMapper; // 注入UserMapper接口

    //通过用户名获取用户信息
    public User getUserByUsername(String username) {
        //使用userMapper获取用户信息
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        User user = userMapper.selectOne(wrapper);
        return user;
    }

    //验证用户名和密码是否匹配
    public boolean validation(String username, String password) {
        //使用userMapper获取用户信息
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        wrapper.eq("password", password);
        User user = userMapper.selectOne(wrapper);
        if (user == null) {
            return false;
        } else {
            return true;
        }
    }

    //注册
    public CommonResponse<String> register(User user) {
       // 先判断用户名是否在数据库中存在
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", user.getUsername());
        User user1 = userMapper.selectOne(wrapper);
        if (user1 != null) {
            return CommonResponse.creatForError( "用户名已存在");
        } else {
            // 生成不重复的uid
            String uid = String.valueOf(System.currentTimeMillis());
            user.setUid(uid);
            // 不存在则插入
            userMapper.insert(user);
            return CommonResponse.creatForSuccess("注册成功");
        }
    }
}
