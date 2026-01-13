package com.vitalia.patient.repository;

import com.vitalia.patient.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for Patient entity
 * TODO: Add findByUserId method
 * TODO: Add search methods with filters
 * TODO: Add findByPhoneNumber method
 */
@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    
    // TODO: Implement custom query methods
    
}
