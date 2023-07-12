package org.makka.greenfarm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.makka.greenfarm.common.CommonResponse;
import org.makka.greenfarm.domain.Owner;
import org.makka.greenfarm.domain.User;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

public interface OwnerService extends IService<Owner> {

    public Owner getOwnerByOwnername(String username);

    //验证用户名和密码是否匹配
    public boolean validation(String name, String password);
    //注册
    public CommonResponse<String> register(Owner owner);

    public String getOidByOwnername(String username);

    public boolean updateAvatar(String oid, HttpServletRequest request, MultipartFile file);

    public boolean updateOwnerInfo(Owner owner);

    public Owner getOwnerInfo(String oid);

    //通过农场主id（ownerid）找到农场id(fid)
    public String getFidByOwnerId(String fid);

}
