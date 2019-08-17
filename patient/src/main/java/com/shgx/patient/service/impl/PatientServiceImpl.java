package com.shgx.patient.service.impl;

import com.shgx.patient.model.Patient;
import com.shgx.patient.repository.PatientRepo;
import com.shgx.patient.service.PatientService;
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
public class PatientServiceImpl implements PatientService {

    @Autowired
    private PatientRepo patientRepo;

    /**
     * 查询信息
     * @param id
     * @return
     */
    @Override
    public Patient queryPatient(Long id){
        Optional<Patient> patient = patientRepo.findById(id);
        if(patient.isPresent()){
            return Patient.builder()
                    .id(id)
                    .content(patient.get().getContent())
                    .commenttime(patient.get().getCommenttime())
                    .build();
        }
        return null;
    }

    /**
     * 保存信息
     * @param patient
     * @return
     */
    @Override
    public Boolean savePatient(Patient patient){
        Optional<List<Patient>> patientDB = patientRepo.findAllById(patient.getId());
        if (patientDB.isPresent()) {
            return true;
        }
        try {
            patient.setCommenttime(new Date());
            save(patient);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 更新信息
     * @param patient
     * @return
     */
    @Override
    public Boolean updatePatient(Patient patient){
        Optional<Patient> patientDB = patientRepo.findById(patient.getId());
        if(patientDB.isPresent() && patient!=null){
            Patient patientTemp = patientDB.get();
            patientTemp.setContent(patient.getContent());
            patientTemp.setCommenttime(new Date());
            save(patientTemp);
        }else{
            log.error("the {} is not in db!", patient.toString());
            return false;
        }
        return true;
    }

    private Patient save(Patient patient){
        patient = patientRepo.save(patient);
        if(patient.getId()<=0){
            log.error("fail to save the patient:{}", patient.toString());
        }
        return patient;
    }
}
