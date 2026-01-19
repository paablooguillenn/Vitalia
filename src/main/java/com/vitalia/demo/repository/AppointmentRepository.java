package com.vitalia.demo.repository;

import com.vitalia.demo.entity.Appointment;
import com.vitalia.demo.entity.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    
    List<Appointment> findByDoctorId(Long doctorId);
    
    List<Appointment> findByPatientId(Long patientId);
    
    List<Appointment> findByStatus(AppointmentStatus status);
    
    List<Appointment> findByDoctorIdAndStatus(Long doctorId, AppointmentStatus status);
    
    List<Appointment> findByPatientIdAndStatus(Long patientId, AppointmentStatus status);
    
    // Buscar citas entre fechas
    List<Appointment> findByDatetimeBetween(LocalDateTime start, LocalDateTime end);
    
    // Query para validar solapamientos
    @Query("SELECT a FROM Appointment a WHERE a.doctor.id = :doctorId " +
           "AND a.datetime BETWEEN :start AND :end " +
           "AND a.status != 'CANCELLED' " +
           "AND (:excludeId IS NULL OR a.id != :excludeId)")
    List<Appointment> findOverlappingAppointments(
        @Param("doctorId") Long doctorId,
        @Param("start") LocalDateTime start,
        @Param("end") LocalDateTime end,
        @Param("excludeId") Long excludeId
    );
}
