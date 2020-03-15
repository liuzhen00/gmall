package com.zl.gmall.auth.controller;

import com.atguigu.core.bean.Resp;
import com.atguigu.core.utils.CookieUtils;
import com.zl.gmall.auth.config.JwtProperties;
import com.zl.gmall.auth.service.AuthService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @author shkstart
 * @create 2020-03-08 1:11
 */
@RestController
@RequestMapping("auth")
@EnableConfigurationProperties(JwtProperties.class)
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtProperties jwtProperties;

    @PostMapping("/accredit")
    public Resp<Object> checkAuth(@RequestParam("username")String username,
                                  @RequestParam("password")String password,
                                  HttpServletRequest request,
                                  HttpServletResponse response){
        //检查用户名和密码是否正确
          String token=   this.authService.checkAuth(username,password);
       //将返回的token信息保存在Cookie中
        if(StringUtils.isBlank(token)){
            return Resp.fail("登录失败，用户名或密码错误");
        }
        System.out.println(jwtProperties.getCookieName());

      //CookieUtils.setCookie(request, response, this.jwtProperties.getCookieName(), token, this.jwtProperties.getExprieTime() * 60);


        Cookie cookie = new Cookie(jwtProperties.getCookieName(),token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(this.jwtProperties.getExprieTime() * 60);

        cookie.setDomain("localhost");
        response.addCookie(cookie);
        String cookieValue = CookieUtils.getCookieValue(request, this.jwtProperties.getCookieName());

        System.out.println("cookesValue:"+cookieValue);



        return Resp.ok("登录成功");
    }
}

