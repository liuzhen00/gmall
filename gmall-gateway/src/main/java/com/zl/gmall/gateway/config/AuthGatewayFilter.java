package com.zl.gmall.gateway.config;

import com.atguigu.core.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author shkstart
 * @create 2020-03-08 11:11
 */
@Component
@EnableConfigurationProperties(JwtProperties.class)
public class AuthGatewayFilter implements GatewayFilter {

    @Autowired
    private JwtProperties jwtProperties;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        //1.从Cookie中获取Jwt类型 token
        MultiValueMap<String, HttpCookie> cookies =
                request.getCookies();
        if(CollectionUtils.isEmpty(cookies)||!cookies.containsKey(jwtProperties.getCookieName())){
              response.setStatusCode(HttpStatus.UNAUTHORIZED);
              return response.setComplete();
        }
        //如果Cookie已经存在,判断它是否为空
        HttpCookie cookie = cookies.getFirst(this.jwtProperties.getCookieName());
        if(cookie==null|| StringUtils.isEmpty(cookie.getValue())){
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }
        try {
            //解析Token
            JwtUtils.getInfoFromToken(cookie.getValue(),this.jwtProperties.getPublicKey());
            //放行
            return chain.filter(exchange);
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.setStatusCode(HttpStatus.UNAUTHORIZED);

        return  response.setComplete();
    }
}
