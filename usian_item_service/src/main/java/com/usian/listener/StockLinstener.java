package com.usian.listener;

import com.rabbitmq.client.Channel;
import com.usian.mapper.ReceiveMessageLogMapper;
import com.usian.mapper.TbItemMapper;
import com.usian.pojo.ReceiveMessageLog;
import com.usian.pojo.TbItem;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * @Title: StockLinstener
 * @Description:
 * @Auther:
 * @Version: 1.0
 * @create 2021/5/28 9:49
 */
@Component
public  class StockLinstener {

    @Autowired
    private TbItemMapper itemMapper;

    @Autowired
    private ReceiveMessageLogMapper receiveMessageLogMapper;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value="stock_queue",durable = "true"),
            exchange = @Exchange(value="deduction_stock_exchange",type= ExchangeTypes.TOPIC),
            key= {"deduction.stock"}
    ))
    public void listen(Map<String,Integer> itemNumMap, Channel channel, Message message){

        // 减库存
        // update  tb_item set num = num - xxx where item_id = xxx?
        //update  tb_item set num = xxx where item_id = xxx?

        String correlationId = message.getMessageProperties().getHeaders().get("spring_returned_message_correlation")+"";
//        message.getMessageProperties().get
        //1. 判断该消息是否已经处理过   消费消息记录表 id主键（）  select * from receive_message_log where id = ??
        ReceiveMessageLog receiveMessageLog = receiveMessageLogMapper.selectByPrimaryKey(correlationId);
        //2. 没有处理过，进行处理
        if(receiveMessageLog==null){
            itemNumMap.forEach((id,num)->{ //num--购买的数量
                itemMapper.updateItemNum(Long.parseLong(id),num);
            });
            receiveMessageLog = new ReceiveMessageLog();
            receiveMessageLog.setId(correlationId);
            receiveMessageLogMapper.insert(receiveMessageLog);
            //3.增加处理过的记录 insert into receive_message_log values()
        }


        //如果处理过，就直接结束


        try {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        } catch (IOException e) {
        }
    }
}
