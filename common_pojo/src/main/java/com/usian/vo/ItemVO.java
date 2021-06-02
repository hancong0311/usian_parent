package com.usian.vo;

import com.usian.pojo.TbItem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Title: ItemVO
 * @Description:
 * @Auther:
 * @Version: 1.0
 * @create 2021/5/11 13:59
 */
@ApiModel("商品对象")
public class ItemVO extends TbItem{  // {item:{tile:xx,name;xxxx,}}


    @ApiModelProperty("商品描述")
    private String desc;
    private String itemParams;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getItemParams() {
        return itemParams;
    }

    public void setItemParams(String itemParams) {
        this.itemParams = itemParams;
    }
}
