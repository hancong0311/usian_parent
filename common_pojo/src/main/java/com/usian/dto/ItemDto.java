package com.usian.dto;

import com.usian.pojo.TbItem;
import com.usian.pojo.TbItemDesc;
import com.usian.pojo.TbItemParam;

import java.io.Serializable;

/**
 * @Title: ItemDto
 * @Description:
 * @Auther:
 * @Version: 1.0
 * @create 2021/5/21 9:13
 */
public class ItemDto implements Serializable {
    private TbItem item;
    private TbItemDesc tbItemDesc;
    private TbItemParam itemParam;
    private String categoryName;

    public TbItem getItem() {
        return item;
    }

    public void setItem(TbItem item) {
        this.item = item;
    }

    public TbItemDesc getTbItemDesc() {
        return tbItemDesc;
    }

    public void setTbItemDesc(TbItemDesc tbItemDesc) {
        this.tbItemDesc = tbItemDesc;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public TbItemParam getItemParam() {
        return itemParam;
    }

    public void setItemParam(TbItemParam itemParam) {
        this.itemParam = itemParam;
    }
}
