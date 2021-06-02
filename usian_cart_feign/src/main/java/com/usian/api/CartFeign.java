package com.usian.api;

import com.usian.vo.CartTermVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Title: CartFeign
 * @Description:
 * @Auther:
 * @Version: 1.0
 * @create 2021/5/25 14:29
 */
@FeignClient("usian-cart-service")
public interface CartFeign {

    @RequestMapping("cart/showCart")
    public List<CartTermVO> getCart(@RequestParam("userId") Long userId);
    @RequestMapping("/cart/addItem")
    public void  addItem(@RequestParam("userId") Long userId, @RequestParam("itemId") Long itemId);
}
