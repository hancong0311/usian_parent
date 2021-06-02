package com.usian.controller;

import com.usian.api.OrderFeign;
import com.usian.pojo.TbOrder;
import com.usian.pojo.TbOrderShipping;
import com.usian.util.Result;
import com.usian.vo.CartTermVO;
import com.usian.vo.OrderVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Title: OrderController
 * @Description:
 * @Auther:
 * @Version: 1.0
 * @create 2021/5/27 10:09
 */
@RestController
@RequestMapping("frontend/order/")
public class OrderController {


    @Autowired
    private OrderFeign orderFeign;

    /**
     * hibernate -validate
     * @param ids
     * @param userId
     * @param token
     * @return
     */
    @RequestMapping("goSettlement")
    public Result goSettlement(@RequestParam("ids") Long[] ids, @RequestParam("userId") Long userId, @RequestParam("token") String token){

        List<CartTermVO> datas =  orderFeign.selectOrderTerms(ids,userId);
        return Result.ok(datas);
    }



    @RequestMapping("insertOrder")
    public Result insertOrder(String orderItem, TbOrderShipping orderShipping, TbOrder order){



       String orderId =  orderFeign.insertOrder(new OrderVO(orderItem,order,orderShipping));
        return Result.ok(orderId);

    }
}
