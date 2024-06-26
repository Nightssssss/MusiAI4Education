package org.musi.AI4Education.config;

import com.baidu.aip.face.AipFace;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BaiduConfig {


    //人脸识别
    @Value("${baidu.appId}")
    private String appId;

    @Value("${baidu.key}")
    private String key;

    @Value("${baidu.secret}")
    private String secret;

    @Bean
    public AipFace aipFace(){
        return new AipFace(appId,key,secret);
    }
}
