package com.usian.vo;

import com.usian.pojo.TbOrder;
import com.usian.pojo.TbOrderShipping;

/**
 * @Title: OrderVO
 * @Description:
 * @Auther:
 * @Version: 1.0
 * @create 2021/5/27 14:18
 */
public class OrderVO {

    private String orderItem;//订单项 json【】
    private TbOrder order;
    private TbOrderShipping orderShipping;

    public String getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(String orderItem) {
        this.orderItem = orderItem;
    }

    public TbOrder getOrder() {
        return order;
    }

    public void setOrder(TbOrder order) {
        this.order = order;
    }

    public TbOrderShipping getOrderShipping() {
        return orderShipping;
    }

    public void setOrderShipping(TbOrderShipping orderShipping) {
        this.orderShipping = orderShipping;
    }

    public OrderVO(String orderItem, TbOrder order, TbOrderShipping orderShipping) {

        this.orderItem = orderItem;
        this.order = order;
        this.orderShipping = orderShipping;
    }

    public OrderVO() {
    }
}
