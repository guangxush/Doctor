package com.shgx.patient.controller;

import com.shgx.common.model.ApiResponse;
import com.shgx.patient.model.Patient;
import com.shgx.patient.pingback.PingBackService;
import com.shgx.patient.service.PatientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author: guangxush
 * @create: 2019/08/17
 */
@RestController
@Slf4j
public class PatientController {

    @Autowired
    private PatientService patientService;

    @Autowired
    private PingBackService pingBackService;

    private final String url = "";

    @RequestMapping(path = "/query/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse<Patient> queryScore(@PathVariable("id") Long id) {
        if (id == null) {
            return new ApiResponse<Patient>().fail(new Patient());
        }
        Patient patient = patientService.queryPatient(id);
        return new ApiResponse<Patient>().success(patient);
    }


    @RequestMapping(path = "/insert", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponse<Boolean> insertBusiness(@RequestBody Patient patient) {
        try{
            Boolean result = patientService.savePatient(patient);
            return new ApiResponse<Boolean>().success(result);
        }catch (InternalError error){
            log.error("insert error");
        }finally {
            pingBackService.jsonRequest(url, patient);
        }
        return null;
    }


    @RequestMapping(path = "/update", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponse<Boolean> updateBusiness(@RequestBody Patient patient) {
        try{
            Boolean result = patientService.updatePatient(patient);
            return new ApiResponse<Boolean>().success(result);
        }catch (InternalError error){
            log.error("update error");
        }finally {
            pingBackService.jsonRequest(url, patient);
        }
        return null;
    }
}
