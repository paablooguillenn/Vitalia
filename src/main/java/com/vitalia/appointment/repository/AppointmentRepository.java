package com.vitalia.appointment.repository;

import com.vitalia.appointment.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for Appointment entity
 * TODO: Add findByDoctorId method
 * TODO: Add findByPatientId method
 * TODO: Add findByDateRange method
 * TODO: Add findByStatus method
 * TODO: Add availability check methods
 */
@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    
    // TODO: Implement custom query methods
    
}
