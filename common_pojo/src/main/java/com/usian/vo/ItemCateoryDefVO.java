package com.usian.vo;

import java.util.List;

/**
 * @Title: ItemCateoryDefVO
 * @Description:
 * @Auther:
 * @Version: 1.0
 * @create 2021/5/14 9:25
 */
public class ItemCateoryDefVO {
    private String n;// 分类的名称
    private List i;//下面的子分类

    public String getN() {
        return n;
    }

    public void setN(String n) {
        this.n = n;
    }

    public List getI() {
        return i;
    }

    public void setI(List i) {
        this.i = i;
    }
}
