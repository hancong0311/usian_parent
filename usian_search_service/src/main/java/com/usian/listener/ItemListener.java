package com.usian.listener;

import com.rabbitmq.client.Channel;
import com.usian.dto.ItemDto;
import com.usian.util.ESUtil;
import com.usian.util.JsonUtils;
import com.usian.vo.ItemESVO;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @Title: ItemListener
 * @Description:
 * @Auther:
 * @Version: 1.0
 * @create 2021/5/21 9:32
 */
@Component
public class ItemListener {

    @Autowired
    private ESUtil esUtil;


    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value="item_queue",durable = "true"),
            exchange = @Exchange(value="item_exchange",type= ExchangeTypes.TOPIC),
            key= {"item.insert"}  // a.b
    ))
    public void insertItem(ItemDto itemDto, Channel channel, Message message){
        ItemESVO itemESVO = new ItemESVO();
        itemESVO.setId(itemDto.getItem().getId());
        itemESVO.setItemTitle(itemDto.getItem().getTitle());
        itemESVO.setItemSellPoint(itemDto.getItem().getSellPoint());
        itemESVO.setItemPrice(itemDto.getItem().getPrice()+"");
        itemESVO.setItemImage(itemDto.getItem().getImage());
        itemESVO.setItemDesc(itemDto.getTbItemDesc().getItemDesc());
        itemESVO.setItemCategoryName(itemDto.getCategoryName());
        String source = JsonUtils.objectToJson(itemESVO);
        // 新增消息到es
        esUtil.insertDoc("usian","item",source);
        try {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        } catch (IOException e) {
        }


    }
}
