package org.makka.greenfarm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.junit.Test;
import org.makka.greenfarm.common.CommonResponse;
import org.makka.greenfarm.domain.User;
import org.makka.greenfarm.mapper.UserMapper;
import org.makka.greenfarm.service.UserService;
import org.makka.greenfarm.utils.UploadAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

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
        return user != null;
    }

    //注册
    public CommonResponse<String> register(@RequestBody User user) {
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
            // 设置默认头像
            if (user.getGender().equals("male")){
                user.setAvatar("http://localhost:8080/images/user/avatar/default_male.png");
            }else if (user.getGender().equals("female")){
                user.setAvatar("http://localhost:8080/images/user/avatar/default_female.png");
            }else{
                user.setAvatar("http://localhost:8080/images/user/avatar/default.png");
            }
            user.setVirtualization("http://175.178.5.157:8080/images/user/virtualization/default.gif");
            // 不存在则插入
            userMapper.insert(user);
            return CommonResponse.creatForSuccess("注册成功");
        }
    }

    public String getUidByUsername(String username) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        User user = userMapper.selectOne(wrapper);
        return user.getUid();
    }

    public boolean updateAvatar(String uid, HttpServletRequest request, MultipartFile file) {
        String avatar = UploadAction.uploadAvatar(request, file);
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("uid", uid);
        User user = userMapper.selectOne(wrapper);
        user.setAvatar(avatar);
        int result = userMapper.updateById(user);
        if (result == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean updateUserInfo(User user) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("uid", user.getUid());
        User user1 = userMapper.selectOne(wrapper);
        user.setUsername(user1.getUsername());
        user.setPassword(user1.getAvatar());
        user.setAvatar(user1.getAvatar());
        user.setVirtualization(user1.getVirtualization());
        int result = userMapper.updateById(user);
        if (result == 1) {
            return true;
        } else {
            return false;
        }
    }

    @Cacheable(value = "users", key = "0")
    public User getUserInfo(String uid) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("uid", uid);
        return userMapper.selectOne(wrapper);
    }

    public String getVirtualizationByUid(String uid) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("uid", uid);
        User user = userMapper.selectOne(wrapper);
        return user.getVirtualization();
    }

    @Override
    public int addUsersByFace(User user) {
        userMapper.insert(user);
        System.out.println(user.getUsername());
        return 1;
    }

    @Override
    public User selectUserByName(String username) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        return userMapper.selectOne(wrapper);
    }

    @Override
    public String getFaceUidByUsername(String username) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        return userMapper.selectOne(wrapper).getUid();
    }
}
