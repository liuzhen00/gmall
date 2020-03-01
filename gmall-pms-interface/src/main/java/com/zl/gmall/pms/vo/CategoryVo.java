package com.zl.gmall.pms.vo;

import com.zl.gmall.pms.entity.CategoryEntity;
import lombok.Data;

import java.util.List;

/**
 * @author shkstart
 * @create 2020-03-01 13:01
 */
@Data
public class CategoryVo extends CategoryEntity {

    private List<CategoryEntity> subs;

}
