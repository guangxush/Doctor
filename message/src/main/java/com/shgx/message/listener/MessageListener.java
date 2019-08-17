package com.shgx.message.listener;

import com.alibaba.fastjson.JSONObject;
import com.shgx.message.model.Message;
import com.shgx.message.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @author: guangxush
 * @create: 2019/08/17
 */
@Component
public class MessageListener {
    private static final Logger log= LoggerFactory.getLogger(MessageListener.class);

    @Autowired
    private MessageService messageService;

    @KafkaListener(id = "forward", topics = "mysql-kafka-patient")
    public String forward(String data) {
        log.info("mysql-kafka-patient "+data);
        JSONObject jsonObject1 = JSONObject.parseObject(data);
        Message message = (Message) JSONObject.toJavaObject(jsonObject1,Message.class);
        messageService.updateMessage(message);
        return data;
    }
}
