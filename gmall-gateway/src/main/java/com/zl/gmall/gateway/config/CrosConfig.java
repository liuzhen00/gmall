package com.zl.gmall.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

/**
 * @author shkstart
 * @create 2020-02-19 10:10
 */
@Configuration
public class CrosConfig {

    @Bean
    public CorsWebFilter crsWebFilter(){
        //初始化config配置对象
        CorsConfiguration config=new CorsConfiguration();
        //允许域
        config.addAllowedOrigin("http://127.0.0.1:1000");
        config.addAllowedOrigin("http://localhost:1000");
        //允许头信息
        config.addAllowedHeader("*");
        //允许请求方法
        config.addAllowedMethod("*");
        //允许携带Cookie消息
        config.setAllowCredentials(true);


        //添加映射路径,拦截所有的映射
        UrlBasedCorsConfigurationSource configurationSource = new UrlBasedCorsConfigurationSource();
        configurationSource.registerCorsConfiguration("/**",config);
        return new CorsWebFilter(configurationSource);

    }
}
