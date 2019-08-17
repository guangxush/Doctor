package com.shgx.pingback.service.impl;

import com.shgx.pingback.model.PingBack;
import com.shgx.pingback.repository.PingBackRepo;
import com.shgx.pingback.service.PingBackService;
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
public class PingBackServiceImpl implements PingBackService {

    @Autowired
    private PingBackRepo pingBackRepo;

    /**
     * 查询信息
     * @param id
     * @return
     */
    @Override
    public PingBack queryPingBack(Long id){
        Optional<PingBack> patient = pingBackRepo.findById(id);
        if(patient.isPresent()){
            return PingBack.builder()
                    .id(id)
                    .content(patient.get().getContent())
                    .commenttime(patient.get().getCommenttime())
                    .build();
        }
        return null;
    }

    /**
     * 保存信息
     * @param pingBack
     * @return
     */
    @Override
    public Boolean savePingBack(PingBack pingBack){
        Optional<List<PingBack>> patientDB = pingBackRepo.findAllById(pingBack.getId());
        if (patientDB.isPresent()) {
            return true;
        }
        try {
            pingBack.setCommenttime(new Date());
            save(pingBack);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 更新信息
     * @param pingBack
     * @return
     */
    @Override
    public Boolean updatePingBack(PingBack pingBack){
        Optional<PingBack> patientDB = pingBackRepo.findById(pingBack.getId());
        if(patientDB.isPresent() && pingBack !=null){
            PingBack pingBackTemp = patientDB.get();
            pingBackTemp.setContent(pingBack.getContent());
            pingBackTemp.setCommenttime(new Date());
            save(pingBackTemp);
        }else{
            log.error("the {} is not in db!", pingBack.toString());
            return false;
        }
        return true;
    }

    private PingBack save(PingBack pingBack){
        pingBack = pingBackRepo.save(pingBack);
        if(pingBack.getId()<=0){
            log.error("fail to save the pingBack:{}", pingBack.toString());
        }
        return pingBack;
    }
}
