package com.shgx.patient.repository;

import com.shgx.patient.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


/**
 * @author: guangxush
 * @create: 2019/08/17
 */
@Repository
public interface PatientRepo extends JpaRepository<Patient, Long> {

    Optional<List<Patient>> findAllById(Long id);
}
