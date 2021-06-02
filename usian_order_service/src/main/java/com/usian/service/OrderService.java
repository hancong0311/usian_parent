package com.usian.service;

import com.alibaba.druid.support.json.JSONUtils;
import com.usian.mapper.SendMessageLogMapper;
import com.usian.mapper.TbOrderItemMapper;
import com.usian.mapper.TbOrderMapper;
import com.usian.mapper.TbOrderShippingMapper;
import com.usian.mq.OrderConfirmAndReturn;
import com.usian.mq.OrderSendMessage;
import com.usian.pojo.SendMessageLog;
import com.usian.pojo.TbOrder;
import com.usian.pojo.TbOrderItem;
import com.usian.pojo.TbOrderShipping;
import com.usian.util.JsonUtils;
import com.usian.util.RedisClient;
import com.usian.vo.CartTermVO;
import com.usian.vo.OrderVO;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Title: OrderService
 * @Description:
 * @Auther:
 * @Version: 1.0
 * @create 2021/5/27 10:38
 */
@Service
public class OrderService {

    @Autowired
    private RedisClient redisClient;

    @Autowired
    private TbOrderMapper orderMapper;

    @Autowired
    private TbOrderItemMapper orderItemMapper;

    @Autowired
    private TbOrderShippingMapper orderShippingMapper;


    @Autowired
    private OrderSendMessage orderSendMessage;

    @Autowired
    private SendMessageLogMapper sendMessageLogMapper;


    /**
     *  从购物车项中，选择用户选择的购买的购物车项
     * @param ids
     * @param userId
     * @return
     */
    public List<CartTermVO> selectOrderTerms(Long[] ids, Long userId) {


        //1. 从redis 中 获取当前用户，所有的购物车项的数据
        List<CartTermVO> cartTerms = redisClient.hgetAll("CART" + userId);

        //2. 筛选出用户选择的购物车项的信息


        // 方式二：1.8 新特性
        //  s: 集合中的某一个元素
        //  ids: 1,2                 cartTerms: 1,2,3
        List<CartTermVO> orderTerms = cartTerms.stream().filter(cartTerm -> {
                // 判断该购物车项 是否是用户选择的
//            for (Long id : ids) {
//                    if(cartTerm.getId().equals(id)){
//                        return true;
//                    }
//              }

            long count = Arrays.stream(ids).filter(id -> {
                if (id.equals(cartTerm.getId())) {
                    return true;
                }
                return false;
            }).count();

            if(count==1){
                return true;
            }
            return false;
        }).collect(Collectors.toList());


        // 方式一： 复杂的写法
//        ArrayList<CartOrOrderTermVO> orderTerms = new ArrayList<>();//选择购买的购物车项集合---  订单项集合
//
//        for (Long id : ids) {
//            for (CartOrOrderTermVO cartTerm : cartTerms) {
//                if(cartTerm.getId().equals(id)){
//                    orderTerms.add(cartTerm);
//                    break;// break 结束当前循环  continue：结束本次循环，执行下次循环
//                }
//            }
//        }


        return orderTerms;

    }

    /**
     * 下订单
     * @param orderVO
     * @return
     */
    @Transactional
    public String insertOrder(OrderVO orderVO) {
        // 订单基本表   1条
        TbOrder order = orderVO.getOrder();
        //生成一个订单ID  一定保证唯一性
        //用户id+当前时间+随机数  分布式的ID   雪花算法
        Date now = new Date();
        String orderId = UUID.randomUUID().toString();
        order.setOrderId(orderId);
        order.setStatus(1);
        order.setCreateTime(now);
        orderMapper.insertSelective(order);

        //订单项  多条
        String orderItems = orderVO.getOrderItem();
        List<TbOrderItem> orderItemList = JsonUtils.jsonToList2(orderItems, TbOrderItem.class);
        orderItemList.forEach(orderItem ->{
            orderItem.setId(UUID.randomUUID().toString());
            orderItem.setOrderId(orderId);
            orderItemMapper.insertSelective(orderItem);
        });

        //订单收获地址  1条
        TbOrderShipping orderShipping = orderVO.getOrderShipping();
        orderShipping.setOrderId(orderId);
        orderShipping.setCreated(now);
        orderShippingMapper.insertSelective(orderShipping);


        //修改库存
        // 保证消息成功的发送出去   发送消息记录表：消费发送的状态 （0：待发送 1：已发送）

        /*

           2. 发消息
           3.  监听mq的返回结果，如果成功了，修改记录表的状态变为1
           4.  一个定时器，实时的扫描记录表中，待发送的数据，进行重新的发送
         */

        // 发送到MQ的消息
        Map<String, Integer> itemNumMap = orderItemList.stream().collect(Collectors.toMap(TbOrderItem::getItemId,TbOrderItem::getNum));


//        1. 发送前，先数据库中新增一条记录表，状态为0：待发送
//        发送消息记录表： 消费发送的状态 （0：待发送 1：已发送），主键，发送的消息本体  日志
        SendMessageLog sendMessageLog = new SendMessageLog();
        sendMessageLog.setBody(JsonUtils.objectToJson(itemNumMap));
        int sendMessagLogId = sendMessageLogMapper.insertSelective(sendMessageLog);//影响的行数

        orderSendMessage.sendMeassge(sendMessageLog.getId()+"",itemNumMap);


        return  orderId;

    }
}
