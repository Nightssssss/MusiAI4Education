package org.makka.greenfarm.service;

import com.alibaba.fastjson.JSON;
import okhttp3.*;
import org.makka.greenfarm.domain.GiteeUser;
import org.makka.greenfarm.domain.ProviderToken;

import java.util.Objects;

public interface GiteeProviderService {

    public String getGiteeToken(ProviderToken providerToken);

    /**
     * 根据用户的 access token 获取当前gitee用户的详细信息
     * @param accessToken 用户的访问令牌
     * @return gitee用户对象
     */
    public GiteeUser getGiteeUser(String accessToken);
}
