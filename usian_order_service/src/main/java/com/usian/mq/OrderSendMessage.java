package com.usian.mq;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Title: OrderSendMessage
 * @Description:
 * @Auther:
 * @Version: 1.0
 * @create 2021/6/1 9:25
 */
@Component
public class OrderSendMessage {

    @Autowired
    private OrderConfirmAndReturn orderConfirmAndReturn;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送修改库存消息
     * @param sendMessagLogId  发送消息日志的ID
     * @param itemNumMap   订单项与库存对应数据
     */
    public void sendMeassge(String sendMessagLogId, Map<String, Integer> itemNumMap){
        //设置回调函数对象的
        rabbitTemplate.setConfirmCallback(orderConfirmAndReturn);
        rabbitTemplate.setReturnCallback(orderConfirmAndReturn);
        //2.往MQ发送消息
        CorrelationData correlationData = new CorrelationData();
        correlationData.setId(sendMessagLogId);

        rabbitTemplate.convertAndSend("deduction_stock_exchange","deduction.stock",itemNumMap,correlationData);
    }
}
