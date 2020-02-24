package com.zl.gmall.pms.vo;

import com.zl.gmall.pms.entity.AttrAttrgroupRelationEntity;
import com.zl.gmall.pms.entity.AttrEntity;
import com.zl.gmall.pms.entity.AttrGroupEntity;
import lombok.Data;

import java.util.List;

/**
 * @author shkstart
 * @create 2020-02-21 19:56
 */
@Data
public class AttrGroupVo extends AttrGroupEntity {
    private List<AttrAttrgroupRelationEntity> relations;
    private List<AttrEntity> attrEntities;
}
