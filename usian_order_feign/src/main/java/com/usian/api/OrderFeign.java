package com.usian.api;

import com.usian.vo.CartTermVO;
import com.usian.vo.OrderVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Title: OrderFeign
 * @Description:
 * @Auther:
 * @Version: 1.0
 * @create 2021/5/27 10:30
 */
@FeignClient("usian-order-service")
public interface OrderFeign {

    @RequestMapping("order/selectOrderTerms")
    public List<CartTermVO> selectOrderTerms(@RequestParam("ids") Long[] ids, @RequestParam("userId") Long userId);

    @RequestMapping("order/insertOrder")
    String insertOrder(@RequestBody OrderVO orderVO);
}
