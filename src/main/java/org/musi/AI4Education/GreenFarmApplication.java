package org.musi.AI4Education;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableCaching
@MapperScan("org.musi.AI4Education.mapper")
public class GreenFarmApplication {
    public static void main(String[] args) {
        SpringApplication.run(GreenFarmApplication.class, args);
    }

}
