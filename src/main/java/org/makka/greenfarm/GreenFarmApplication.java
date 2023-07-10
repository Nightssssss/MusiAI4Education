package org.makka.greenfarm;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
@MapperScan("org.makka.greenfarm.mapper")
public class GreenFarmApplication {
    public static void main(String[] args) {
        SpringApplication.run(GreenFarmApplication.class, args);
    }

}
