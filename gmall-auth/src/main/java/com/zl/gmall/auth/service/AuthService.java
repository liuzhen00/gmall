package com.zl.gmall.auth.service;

import com.atguigu.core.bean.Resp;
import com.atguigu.core.utils.JwtUtils;
import com.zl.gmall.auth.config.JwtProperties;
import com.zl.gmall.auth.feign.GmallUmsClient;
import com.zl.gmall.ums.entity.MemberEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author shkstart
 * @create 2020-03-08 1:12
 */
@Service
public class AuthService {

    @Autowired
    private GmallUmsClient gmallUmsClient;

    @Autowired
    private JwtProperties jwtProperties;
    public String checkAuth(String username, String password) {
        //查询用户名或者密码是否正确
        Resp<MemberEntity> memberEntityResp = gmallUmsClient.queryUserNameAndPword(username, password);
        MemberEntity memberEntity = memberEntityResp.getData();
        if(memberEntity==null){
            return null;
        }

        //生成token信息
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("id",memberEntity.getId());
        hashMap.put("name",memberEntity.getUsername());
        try {
            return JwtUtils.generateToken(hashMap, jwtProperties.getPrivateKey(), jwtProperties.getExprieTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }
}
