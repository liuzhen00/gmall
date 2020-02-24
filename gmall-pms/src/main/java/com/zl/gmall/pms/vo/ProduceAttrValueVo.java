package com.zl.gmall.pms.vo;

import com.zl.gmall.pms.entity.ProductAttrValueEntity;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author shkstart
 * @create 2020-02-23 10:46
 */

public class ProduceAttrValueVo extends ProductAttrValueEntity {

    //因为JSON接受的属性值为：valueSelected
    public void setValueSelected(List<Object> valueSelected){
        if(CollectionUtils.isEmpty(valueSelected)){
            return ;
        }

        this.setAttrValue( StringUtils.join(valueSelected,","));
    }

}
