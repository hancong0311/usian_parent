package com.usian.mq;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import com.usian.mapper.SendMessageLogMapper;
import com.usian.pojo.SendMessageLog;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Title: OrderConfirmAndReturn
 * @Description:
 * @Auther:
 * @Version: 1.0
 * @create 2021/6/1 9:12
 */
@Component
public class OrderConfirmAndReturn implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback {

    @Autowired
    private SendMessageLogMapper sendMessageLogMapper;
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if(ack){//发送成功了
            //修改发送消息的状态 为 1

            SendMessageLog sendMessageLog = new SendMessageLog();
            sendMessageLog.setStatus(1);
            sendMessageLog.setId(Long.parseLong(correlationData.getId())); //消息的主键  ???
            System.err.println("-------correlationData----------"+correlationData.getId());
            sendMessageLogMapper.updateByPrimaryKeySelective(sendMessageLog);


        }

    }

    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        System.out.println("虚拟机问题。。。。。。。");
    }
}
