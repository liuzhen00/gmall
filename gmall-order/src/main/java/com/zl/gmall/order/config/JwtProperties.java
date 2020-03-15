package com.zl.gmall.order.config;

import com.atguigu.core.utils.RsaUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.PublicKey;

/**
 * @author shkstart
 * @create 2020-03-08 0:43
 */
@Data
@ConfigurationProperties(prefix = "auth.jwt")
@Slf4j
@Component
public class JwtProperties {


    private String publicKeyPath;

    private String cookieName;

    private PublicKey publicKey;

    private Integer expire;


    //建立初始化方法
    @PostConstruct
    public void init() {
        try {
            this.publicKey = RsaUtils.getPublicKey(publicKeyPath);
        } catch (Exception e) {
            log.error("公钥读取失败");
        }

    }

}
