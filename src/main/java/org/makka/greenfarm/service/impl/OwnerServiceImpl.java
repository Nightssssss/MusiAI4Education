package org.makka.greenfarm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.makka.greenfarm.common.CommonResponse;
import org.makka.greenfarm.domain.Owner;
import org.makka.greenfarm.domain.User;
import org.makka.greenfarm.mapper.OwnerMapper;
import org.makka.greenfarm.mapper.UserMapper;
import org.makka.greenfarm.service.OwnerService;
import org.makka.greenfarm.utils.UploadAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@Service
public class OwnerServiceImpl extends ServiceImpl<OwnerMapper, Owner> implements OwnerService {
    @Autowired
    private OwnerMapper ownerMapper; // 注入UserMapper接口

    @Override
    public Owner getOwnerByOwnername(String username) {
        //使用userMapper获取用户信息
        QueryWrapper<Owner> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        Owner owner = ownerMapper.selectOne(wrapper);
        return owner;
    }

    @Override
    public boolean validation(String username, String password) {
        //使用userMapper获取用户信息
        QueryWrapper<Owner> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        wrapper.eq("password", password);
        Owner owner = ownerMapper.selectOne(wrapper);
        if (owner == null) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public CommonResponse<String> register(Owner owner) {
        // 先判断用户名是否在数据库中存在
        QueryWrapper<Owner> wrapper = new QueryWrapper<>();
        wrapper.eq("username", owner.getUsername());
        Owner owner1 = ownerMapper.selectOne(wrapper);
        if (owner1 != null) {
            return CommonResponse.creatForError( "农场主已存在");
        } else {
            // 生成不重复的oid
            String oid = String.valueOf(System.currentTimeMillis());
            owner.setOid(oid);
            owner.setAvatar("http://localhost:80/images/owner/avatar/default.png");
            // 不存在则插入
            ownerMapper.insert(owner);
            return CommonResponse.creatForSuccess("注册成功");
        }
    }

    @Override
    public String getOidByOwnername(String username) {
        QueryWrapper<Owner> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        Owner owner = ownerMapper.selectOne(wrapper);
        return owner.getOid();
    }

    @Override
    public boolean updateAvatar(String oid, HttpServletRequest request, MultipartFile file) {
        String avatar = UploadAction.uploadOwnerAvatar(request, file) + "";
        QueryWrapper<Owner> wrapper = new QueryWrapper<>();
        wrapper.eq("oid", oid);
        Owner owner = ownerMapper.selectOne(wrapper);
        owner.setAvatar(avatar);
        int result = ownerMapper.updateById(owner);
        if (result == 1) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean updateOwnerInfo(Owner owner) {
        QueryWrapper<Owner> wrapper = new QueryWrapper<>();
        wrapper.eq("oid", owner.getOid());
        Owner owner1 = ownerMapper.selectOne(wrapper);
        owner.setUsername(owner1.getUsername());
        owner.setPassword(owner1.getAvatar());
        owner.setAvatar(owner1.getAvatar());
        int result = ownerMapper.updateById(owner);
        if (result == 1) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Owner getOwnerInfo(String oid) {
        QueryWrapper<Owner> wrapper = new QueryWrapper<>();
        wrapper.eq("oid", oid);
        return ownerMapper.selectOne(wrapper);
    }

    @Override
    public String getFidByOwnerId(String ownerid) {
        QueryWrapper<Owner> wrapper = new QueryWrapper<>();
        wrapper.eq("oid", ownerid);
        return ownerMapper.selectOne(wrapper).getFid();
    }
}
