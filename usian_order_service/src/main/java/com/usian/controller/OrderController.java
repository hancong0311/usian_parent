package com.usian.controller;

import com.usian.service.OrderService;
import com.usian.vo.CartTermVO;
import com.usian.vo.OrderVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Title: OrderController
 * @Description:
 * @Auther:
 * @Version: 1.0
 * @create 2021/5/27 10:37
 */
@RestController
@RequestMapping("order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @RequestMapping("selectOrderTerms")
    public List<CartTermVO> selectOrderTerms(@RequestParam("ids") Long[] ids, @RequestParam("userId") Long userId){
        return orderService.selectOrderTerms(ids,userId);
    }

    @RequestMapping("/insertOrder")
    String insertOrder(@RequestBody OrderVO orderVO){
        return orderService.insertOrder(orderVO);
    }
}
