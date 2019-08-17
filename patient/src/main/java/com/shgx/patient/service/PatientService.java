package com.shgx.patient.service;

import com.shgx.patient.model.Patient;

/**
 * @author: guangxush
 * @create: 2019/08/17
 */
public interface PatientService {
    /**
     * 查询信息
     * @param id
     * @return
     */
    Patient queryPatient(Long id);

    /**
     * 保存信息
     * @param patient
     * @return
     */
    Boolean savePatient(Patient patient);

    /**
     * 更新信息
     * @param patient
     * @return
     */
    Boolean updatePatient(Patient patient);
}
