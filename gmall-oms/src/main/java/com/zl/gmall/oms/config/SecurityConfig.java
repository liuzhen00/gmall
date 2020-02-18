package com.zl.gmall.oms.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author shkstart
 * @create 2020-02-18 23:55
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //释放所有的权限
        http.authorizeRequests().antMatchers("/**").permitAll();
        //禁用掉crsf
        http.csrf().disable();
    }
}
