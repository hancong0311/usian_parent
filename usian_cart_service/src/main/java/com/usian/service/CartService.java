package com.usian.service;

import com.usian.mapper.TbItemMapper;
import com.usian.pojo.TbItem;
import com.usian.util.RedisClient;
import com.usian.vo.CartTermVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Title: CartService
 * @Description:
 * @Auther:
 * @Version: 1.0
 * @create 2021/5/25 14:31
 */
@Service
public class CartService {

    @Autowired
    private RedisClient redisClient;

    @Autowired
    private TbItemMapper itemMapper;

    /**
     * 加入购物车
     * @param userId
     * @param itemId
     * String--->Map
     * Hash
     *      key(String)      value(Hash)
     *                        hkey：商品ID   hvalue:购物车项
     *      "CART"+userId      itemID            CartTermVO
     *
     *      1.  测试用例
     *              1      1001-----》CART1,1001,cartTermVO（num:1）   第一次
     *              1      1001----》CART1,1001,cartTermVO（num:2）  第二次
     *              1      1002----》CART1,  1002，cartTermVO（num:1）  第三次
     *
     *
     */
    public void addItem(Long userId, Long itemId) {
        // 判断用户是否是第一次使用购物车
        Boolean exists = redisClient.exists("CART" + userId);
        if(exists){//不是第一次使用购物车
            //判断该商品是否已在购物车
            CartTermVO cartTermVO = (CartTermVO) redisClient.hget("CART" + userId, itemId + "");
            if(cartTermVO==null) {
                redisClient.hset("CART"+userId,itemId+"",getCartTerm(itemId));
            }else {
                cartTermVO.setNum(cartTermVO.getNum()+1);
                redisClient.hset("CART"+userId,itemId+"",cartTermVO);
            }
        }else{//第一次使用购物车
            redisClient.hset("CART"+userId,itemId+"",getCartTerm(itemId));
        }

    }

    /**
     * 根据商品ID ，获取对应购物车项
     * @param itemId
     * @return
     */
    public CartTermVO getCartTerm(Long itemId){
        TbItem item = itemMapper.selectByPrimaryKey(itemId);
        CartTermVO cartTermVO = new CartTermVO();
        cartTermVO.setId(itemId);
        cartTermVO.setImage(item.getImage());
        cartTermVO.setNum(1);
        cartTermVO.setPrice(item.getPrice());
        cartTermVO.setTitle(item.getTitle());
        cartTermVO.setSellPoint(item.getSellPoint());

        return cartTermVO;
    }

    /**
     * 获取某个人的购物车信息
     * @param userId
     * @return
     */
    public List<CartTermVO> getCart(Long userId) {
        List result = redisClient.hgetAll("CART"+userId);

        return result;
    }
}
