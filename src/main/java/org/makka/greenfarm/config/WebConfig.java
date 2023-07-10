package org.makka.greenfarm.config;

import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    /**
     * 图片保存路径，自动从yml文件中获取数据
     */
    private String fileSavePath = "/home/lighthouse/images/";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        /**
         * 配置资源路径映射
         */
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:"+fileSavePath);
    }
}
