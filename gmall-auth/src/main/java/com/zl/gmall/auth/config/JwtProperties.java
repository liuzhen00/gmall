package com.zl.gmall.auth.config;

import com.atguigu.core.utils.RsaUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @author shkstart
 * @create 2020-03-08 0:43
 */
@Data
@ConfigurationProperties("auth.jwt")
@Slf4j
@Component
public class JwtProperties {

    private String privateKeyPath;
    private String publicKeyPath;
    private String secret;
    private String cookieName;
    private Integer exprieTime;

    //将路径下对应为公钥和私钥封装成一个对象
    private PublicKey publicKey;
    private PrivateKey privateKey;

    //建立初始化方法
    @PostConstruct
    public void init()  {
        try {
        File priKey = new File(privateKeyPath);
        File pubKey = new File(publicKeyPath);
        //如果公钥和私钥的存在在，就去生成一个
        if(!priKey.exists()||!pubKey.exists()){
                RsaUtils.generateKey(publicKeyPath,privateKeyPath,secret);
        }
//        如果存在就读取公私钥
            this.privateKey = RsaUtils.getPrivateKey(privateKeyPath);
            this.publicKey=RsaUtils.getPublicKey(publicKeyPath);
        } catch (Exception e) {
           log.error("公私钥生成失败");
        }

    }

}
