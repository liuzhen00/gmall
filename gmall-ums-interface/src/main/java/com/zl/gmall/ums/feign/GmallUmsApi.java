package com.zl.gmall.ums.feign;

import com.atguigu.core.bean.Resp;
import com.zl.gmall.ums.entity.MemberEntity;
import com.zl.gmall.ums.entity.MemberReceiveAddressEntity;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author shkstart
 * @create 2020-03-08 1:07
 */
public interface GmallUmsApi {

    //根据用户名查询密码和用户
    @GetMapping("ums/member/query")
    public Resp<MemberEntity> queryUserNameAndPword(@RequestParam("username")String username,
                                                    @RequestParam("password")String password);
    @GetMapping("ums/memberreceiveaddress/{memberId}")
    public Resp<List<MemberReceiveAddressEntity>> queryAddressByMemberId(@PathVariable("memberId")Long memberId);


    @GetMapping("ums/member/info/{id}")
    public Resp<MemberEntity> queryMemberById(@PathVariable("id") Long id);


}
