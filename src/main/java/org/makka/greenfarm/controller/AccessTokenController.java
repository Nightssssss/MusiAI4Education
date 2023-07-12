package org.makka.greenfarm.controller;
import org.makka.greenfarm.domain.GiteeUser;
import org.makka.greenfarm.domain.ProviderToken;
import org.makka.greenfarm.service.GiteeProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/gitee")
public class AccessTokenController {
    @Autowired
    private GiteeProviderService giteeProviderService;

    @Value("${client_id}")
    private String clientId;

    @Value("${redirect_uri}")
    private String redirectUri;

    @Value("${client_secret}")
    private String clientSecret;

    @GetMapping("/index")
    public String index() {
        return "index";
    }

    @GetMapping("/callback")
    public String callback(@RequestParam("code") String code,
                           @RequestParam("state") String state,
                           Model model) {

        //用户授权信息
        ProviderToken token = new ProviderToken();
        token.setClientId(clientId);
        token.setRedirectUri(redirectUri);
        token.setClientSecret(clientSecret);
        token.setCode(code);
        token.setState(state);

        //获取token和登录的用户信息
        String accessToken = giteeProviderService.getGiteeToken(token);
        GiteeUser giteeUser = giteeProviderService.getGiteeUser(accessToken);
        System.out.println(giteeUser);

        //要改成统一响应类
        return "index";
    }
}
