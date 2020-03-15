package com.zl.gmall.ums.service.impl;

import com.atguigu.core.exception.MemberException;
import javafx.scene.media.MediaException;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.Query;
import com.atguigu.core.bean.QueryCondition;

import com.zl.gmall.ums.dao.MemberDao;
import com.zl.gmall.ums.entity.MemberEntity;
import com.zl.gmall.ums.service.MemberService;



@Service("memberService")
public class MemberServiceImpl extends ServiceImpl<MemberDao, MemberEntity> implements MemberService {

    @Override
    public PageVo queryPage(QueryCondition params) {
        IPage<MemberEntity> page = this.page(
                new Query<MemberEntity>().getPage(params),
                new QueryWrapper<MemberEntity>()
        );

        return new PageVo(page);
    }

    @Override
    public Boolean checkData(String data, Integer type) {
        QueryWrapper qr=new QueryWrapper();
        /*
          校验手机号或者是邮箱或者是用户名是否备用
         */
        switch (type){
            case 1:
                qr.eq("username",data);
                break;
            case 2:
                qr.eq("mobile",data);
                break;
            case 3:
                qr.eq("email",data);
                break;
                default:
                    return null;
        }

        return this.count(qr)==0;
    }

    @Override
    public void registerUser(MemberEntity memberEntity, String code) {

        //校正验证码

        //生成盐给密码加上唯一ID
        String salt = UUID.randomUUID().toString().substring(0, 5);
        memberEntity.setSalt(salt);


        //加盐加密
        memberEntity.setPassword(DigestUtils.md5Hex(memberEntity.getPassword()+salt));

        //新增用户
        memberEntity.setCreateTime(new Date());
        memberEntity.setIntegration(1000);
        memberEntity.setGrowth(1000);
        memberEntity.setStatus(1);
        memberEntity.setLevelId(0l);
        this.save(memberEntity);

        //删除redis的验证码

    }

    @Override
    public MemberEntity queryUserNameAndPword(String username, String password) {
        //根据用户查询用户
        MemberEntity memberEntity = this.getOne(new QueryWrapper<MemberEntity>().eq("username", username));
        if(memberEntity==null){
            throw new MemberException("用户名输入有误");
        }
        //比较密码
        System.out.println("密码："+DigestUtils.md5Hex(password + memberEntity.getSalt()));
        password = DigestUtils.md5Hex(password + memberEntity.getSalt());
        if(!StringUtils.equals(memberEntity.getPassword(),password)){
            throw new MemberException("密码输入有误");
        }
        return memberEntity;
    }

}