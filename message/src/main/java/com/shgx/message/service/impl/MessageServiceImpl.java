package com.shgx.message.service.impl;

import com.shgx.message.model.Message;
import com.shgx.message.repository.MessageRepo;
import com.shgx.message.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author: guangxush
 * @create: 2019/08/17
 */
@Service
@Slf4j
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageRepo messageRepo;

    /**
     * 查询信息
     * @param id
     * @return
     */
    @Override
    public Message queryMessage(Long id){
        Optional<Message> patient = messageRepo.findById(id);
        if(patient.isPresent()){
            return Message.builder()
                    .id(id)
                    .content(patient.get().getContent())
                    .commenttime(patient.get().getCommenttime())
                    .build();
        }
        return null;
    }

    /**
     * 保存信息
     * @param message
     * @return
     */
    @Override
    public Boolean saveMessage(Message message){
        Optional<List<Message>> patientDB = messageRepo.findAllById(message.getId());
        if (patientDB.isPresent()) {
            return true;
        }
        try {
            message.setCommenttime(new Date());
            save(message);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 更新信息
     * @param message
     * @return
     */
    @Override
    public Boolean updateMessage(Message message){
        Optional<Message> patientDB = messageRepo.findById(message.getId());
        if(patientDB.isPresent() && message !=null){
            Message messageTemp = patientDB.get();
            messageTemp.setContent(message.getContent());
            messageTemp.setCommenttime(new Date());
            save(messageTemp);
        }else{
            log.error("the {} is not in db!", message.toString());
            return false;
        }
        return true;
    }

    private Message save(Message message){
        message = messageRepo.save(message);
        if(message.getId()<=0){
            log.error("fail to save the message:{}", message.toString());
        }
        return message;
    }
}
