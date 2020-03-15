package com.zl.gmall.pms;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@MapperScan(basePackages = "com.zl.gmall.pms.dao")
@EnableDiscoveryClient
@RefreshScope
@EnableFeignClients
@Slf4j
public class GmallPmsApplication {

    public static void main(String[] args) {

        SpringApplication.run(GmallPmsApplication.class, args);
    }

}
