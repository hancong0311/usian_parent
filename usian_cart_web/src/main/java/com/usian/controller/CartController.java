package com.usian.controller;

import com.usian.api.CartFeign;
import com.usian.api.ItemFeign;
import com.usian.pojo.TbItem;
import com.usian.util.CookieUtils;
import com.usian.util.JsonUtils;
import com.usian.util.Result;
import com.usian.vo.CartTermVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @Title: CartController
 * @Description:
 * @Auther:
 * @Version: 1.0
 * @create 2021/5/25 9:12
 */
@RestController
@RequestMapping("/frontend/cart/")
public class CartController {

    @Autowired
    private ItemFeign itemFeign;//查询商品微服务的feign

    @Autowired
    private CartFeign cartFeign;


    @RequestMapping("showCart")
    public Result showCart(@RequestParam("userId") Long userId,HttpServletRequest request){

        if(userId==null){
            String cartJson = CookieUtils.getCookieValue(request, "CART", true);
            Map<Long, CartTermVO> cartMap = JsonUtils.jsonToMap(cartJson,Long.class, CartTermVO.class);

            Collection<CartTermVO> cartTerms = cartMap.values();
            return Result.ok(cartTerms);

        }else{//登录
            List<CartTermVO> cartTerms = cartFeign.getCart(userId);

            return Result.ok(cartTerms);
        }
    }

    /**
     * 添加商品（只加1件）到购物车
     * @param userId
     * @param itemId
     *
     *   request：获取当前浏览器已有的cookie
     *   response:可以更改浏览器中cookie的值
     * @return
     */
    @RequestMapping("/addItem")
    public Result  addItem(@RequestParam("userId") Long userId, @RequestParam("itemId") Long itemId, HttpServletRequest request, HttpServletResponse response){
        /*
                1. 判断是登录
                  没登陆：  cookie   web服务
         */
        if(userId==null){// 没登陆：  cookie   web服务
            // 往浏览器中的cookie去存储值
            // name  value
            // "CART"   Map(key:商品id  value：购物车项)
               //1. 判断用户是否是第一次使用购物车
            String cartJson = CookieUtils.getCookieValue(request, "CART", true);
            if(cartJson == null || cartJson.equals("")){
                //1.1 如果是第一次使用购物车，创建一个购物车的cookie,网cookie增加购物车项目
                HashMap<Long, CartTermVO> cartMap = new HashMap<>();
                //新增商品到购物车
                addCartTerm(itemId,cartMap);

                CookieUtils.setCookie(request,response,"CART", JsonUtils.objectToJson(cartMap),true);

            }else { // 1.2 如果不是第一次，获取上一次的购物车cookie的值，往里面追加新的内容即可
                Map<Long, CartTermVO> cart = JsonUtils.jsonToMap(cartJson, Long.class, CartTermVO.class);
                // 判断该购买的商品是否已经加入过购物车
                CartTermVO cartTermVO = cart.get(itemId);
                if (cartTermVO == null) {
                    //没有加入过，新增一个购物车项
                    addCartTerm(itemId, cart);
                } else {
                    // 如果加入过，需要修改数量（原有数量+1）
                    cartTermVO.setNum(cartTermVO.getNum() + 1);
                }
                //将修改后的购物车，更新都coookie
                CookieUtils.setCookie(request, response, "CART", JsonUtils.objectToJson(cart), true);
            }

        }else{//登录：redis  service服务

            cartFeign.addItem(userId,itemId);

        }

        return Result.ok();
    }


    /**
     * 新增购物车项
     * @param itemId
     * @param cartMap
     */
    public void addCartTerm(Long itemId,Map cartMap){
        TbItem item = itemFeign.selectItemInfo(itemId);
        CartTermVO cartTermVO = new CartTermVO();
        cartTermVO.setId(itemId);
        cartTermVO.setImage(item.getImage());
        cartTermVO.setNum(1);
        cartTermVO.setPrice(item.getPrice());
        cartTermVO.setTitle(item.getTitle());
        cartTermVO.setSellPoint(item.getSellPoint());

        cartMap.put(itemId,cartTermVO);
    }
}
