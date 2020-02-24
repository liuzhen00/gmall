package com.zl.gmall.pms.service.impl;

import com.zl.gmall.pms.vo.SpuInfoVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.Query;
import com.atguigu.core.bean.QueryCondition;

import com.zl.gmall.pms.dao.SpuInfoDescDao;
import com.zl.gmall.pms.entity.SpuInfoDescEntity;
import com.zl.gmall.pms.service.SpuInfoDescService;
import org.springframework.transaction.annotation.Transactional;


@Service("spuInfoDescService")
public class SpuInfoDescServiceImpl extends ServiceImpl<SpuInfoDescDao, SpuInfoDescEntity> implements SpuInfoDescService {

    @Override
    public PageVo queryPage(QueryCondition params) {
        IPage<SpuInfoDescEntity> page = this.page(
                new Query<SpuInfoDescEntity>().getPage(params),
                new QueryWrapper<SpuInfoDescEntity>()
        );

        return new PageVo(page);
    }

    @Transactional
    public Long saveSpuInfoDesc(SpuInfoVo spuInfoVo) {
        //获取新增后的spuId
        Long spuId = spuInfoVo.getId();
        //1.2保存SPU的描述信息spu_info_desc
        SpuInfoDescEntity spuInfoDescEntity = new SpuInfoDescEntity();
        spuInfoDescEntity.setSpuId(spuId);
        spuInfoDescEntity.setDecript(StringUtils.join(spuInfoVo.getSpuImages(),","));

        this.save(spuInfoDescEntity);
        return spuId;
    }

}