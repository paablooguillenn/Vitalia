package com.vitalia.doctor.repository;

import com.vitalia.doctor.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for Doctor entity
 * TODO: Add findByUserId method
 * TODO: Add findBySpecialization method
 * TODO: Add findByLicenseNumber method
 * TODO: Add search methods with filters
 */
@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    
    // TODO: Implement custom query methods
    
}
