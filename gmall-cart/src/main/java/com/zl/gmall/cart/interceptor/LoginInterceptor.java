package com.zl.gmall.cart.interceptor;

import com.atguigu.core.utils.CookieUtils;
import com.atguigu.core.utils.JwtUtils;
import com.zl.gmall.cart.config.JwtProperties;
import com.zl.gmall.cart.entity.UserInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.UUID;

/**
 * @author shkstart
 * @create 2020-03-09 11:41
 */
@Component
@EnableConfigurationProperties({JwtProperties.class})
public class LoginInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private JwtProperties jwtProperties;

    private static final ThreadLocal<UserInfo> THREAD_LOCAL=new ThreadLocal<>();

    //方法拦截之前执行

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //获取Cookie信息
        String token = CookieUtils.getCookieValue(request, jwtProperties.getCookieName());
        String userKey = CookieUtils.getCookieValue(request, jwtProperties.getUserKeyName());
       //如果都为空则设置
        if(StringUtils.isEmpty(userKey)){
            //设置userKey
            userKey=UUID.randomUUID().toString();

            Cookie cookie = new Cookie(jwtProperties.getUserKeyName(),userKey);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(this.jwtProperties.getExpire() * 60);

            cookie.setDomain("localhost");
            response.addCookie(cookie);
          // CookieUtils.setCookie(request,response,jwtProperties.getUserKeyName(),userKey,jwtProperties.getExpire());
        }

        //不管有没有key都需要设置userKey
          UserInfo userInfo=new UserInfo();
          userInfo.setUserKey(userKey);

//          当token不为空的联系token
        if(StringUtils.isNotBlank(token)){
            Map<String, Object> infoFromToken = JwtUtils.getInfoFromToken(token, jwtProperties.getPublicKey());
            if(!CollectionUtils.isEmpty(infoFromToken)){
                userInfo.setId(new Long(infoFromToken.get("id").toString()));
            }
        }

        //将解析的userInfo保存在ThreadLocal
        THREAD_LOCAL.set(userInfo);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        THREAD_LOCAL.remove();
    }

    public static UserInfo getUserInfo(){
        return THREAD_LOCAL.get();
    }
}
