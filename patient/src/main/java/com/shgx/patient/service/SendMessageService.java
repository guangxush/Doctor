package com.shgx.patient.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shgx.patient.model.Patient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * @author: guangxush
 * @create: 2019/08/17
 */
@Service
@Slf4j
public class SendMessageService {
    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Value("topic")
    private String topic;

    private ObjectMapper om = new ObjectMapper();

    public boolean send(Object object){
        String objectJson = "";
        try {
            objectJson = new ObjectMapper().writeValueAsString(object);
        } catch (Exception e) {
            log.error("can't trans the {} object to json string!", object);
            return false;
        }
        try{
            String result = kafkaTemplate.send("mysql-kafka-patient", objectJson).get().toString();
            if(result!=null){
                return true;
            }

        }catch (Exception e){
            return false;
        }
        return false;
    }
}
