package com.usian.task;

import com.netflix.discovery.converters.Auto;
import com.usian.mapper.SendMessageLogMapper;
import com.usian.mapper.TbOrderMapper;
import com.usian.mq.OrderSendMessage;
import com.usian.pojo.SendMessageLog;
import com.usian.util.JsonUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Title: CloseOverTimeOrder
 * @Description:
 * @Auther:
 * @Version: 1.0
 * @create 2021/5/28 10:12
 */
@Component
public class CloseOverTimeOrder {

    @Autowired
    private TbOrderMapper orderMapper;

    @Autowired
    private SendMessageLogMapper sendMessageLogMapper;


    @Autowired
    private OrderSendMessage orderSendMessage;

    @Scheduled(cron="0 0/1 * * * ?")
    public void task(){
        System.out.println("-------------------------");
        // 关闭超时订单
        //方式一
        //1. 去读数据库，未支付 超时的订单  select * from tb_order where status = 1 and  now-create_time>=30分钟
        //2. 关闭这些符合条件的订单   update tb_order set status = 6 where order_id = xxx

        // 方式二： 二合一
        orderMapper.closeOverTimeOrders();
    }

    @Scheduled(cron="0/10 * * * * ?")
        public void sendOrderMesaage(){
            //1. 读取代发送的消息 select * from send_message_log where  status = 0;

            SendMessageLog condition = new SendMessageLog();
            condition.setStatus(0);

            List<SendMessageLog> datas = sendMessageLogMapper.select(condition);

            //2.重新 mq发消息
            datas.forEach(message->{
//            rabbitTemplate.convertAndSend("deduction_stock_exchange","deduction.stock",itemNumMap,correlationData);

                orderSendMessage.sendMeassge(message.getId()+"", JsonUtils.jsonToMap(message.getBody(),String.class,Integer.class));

            });
    }

}
