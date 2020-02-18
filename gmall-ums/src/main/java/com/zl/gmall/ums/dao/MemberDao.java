package com.zl.gmall.ums.dao;

import com.zl.gmall.ums.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author zl
 * @email lz_liuzhen00@163.com
 * @date 2020-02-18 22:42:00
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
