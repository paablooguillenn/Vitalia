package com.vitalia.demo.service;

import com.vitalia.demo.dto.AppointmentRequest;
import com.vitalia.demo.dto.AppointmentResponse;
import com.vitalia.demo.entity.Appointment;
import com.vitalia.demo.entity.AppointmentStatus;
import com.vitalia.demo.entity.Doctor;
import com.vitalia.demo.entity.Patient;
import com.vitalia.demo.repository.AppointmentRepository;
import com.vitalia.demo.repository.DoctorRepository;
import com.vitalia.demo.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    
    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    
    /**
     * Crear una nueva cita
     */
    @Transactional
    public AppointmentResponse createAppointment(AppointmentRequest request) {
        // Validar que el doctor existe
        Doctor doctor = doctorRepository.findById(request.getDoctorId())
            .orElseThrow(() -> new RuntimeException("Doctor no encontrado"));
        
        // Validar que el paciente existe
        Patient patient = patientRepository.findById(request.getPatientId())
            .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));
        
        // Validar solapamientos
        if (hasOverlap(request.getDoctorId(), request.getDatetime(), null)) {
            throw new RuntimeException("El doctor ya tiene una cita en ese horario");
        }
        
        // Validar que la fecha no sea pasada
        if (request.getDatetime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("No se puede agendar una cita en el pasado");
        }
        
        // Crear la cita
        Appointment appointment = new Appointment();
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setDatetime(request.getDatetime());
        appointment.setNotes(request.getNotes());
        appointment.setStatus(AppointmentStatus.CREATED);
        
        Appointment saved = appointmentRepository.save(appointment);
        
        return mapToResponse(saved);
    }
    
    /**
     * Obtener todas las citas
     */
    public List<AppointmentResponse> getAllAppointments() {
        return appointmentRepository.findAll().stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }
    
    /**
     * Obtener cita por ID
     */
    public AppointmentResponse getAppointmentById(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Cita no encontrada"));
        return mapToResponse(appointment);
    }
    
    /**
     * Obtener citas por doctor
     */
    public List<AppointmentResponse> getAppointmentsByDoctor(Long doctorId) {
        return appointmentRepository.findByDoctorId(doctorId).stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }
    
    /**
     * Obtener citas por paciente
     */
    public List<AppointmentResponse> getAppointmentsByPatient(Long patientId) {
        return appointmentRepository.findByPatientId(patientId).stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }
    
    /**
     * Obtener citas por estado
     */
    public List<AppointmentResponse> getAppointmentsByStatus(AppointmentStatus status) {
        return appointmentRepository.findByStatus(status).stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }
    
    /**
     * Actualizar/Reprogramar una cita
     */
    @Transactional
    public AppointmentResponse updateAppointment(Long id, AppointmentRequest request) {
        Appointment appointment = appointmentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Cita no encontrada"));
        
        // Validar que no esté cancelada o completada
        if (appointment.getStatus() == AppointmentStatus.CANCELLED || 
            appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new RuntimeException("No se puede modificar una cita cancelada o completada");
        }
        
        // Si cambió el doctor o la fecha, validar solapamientos
        if (!appointment.getDoctor().getId().equals(request.getDoctorId()) || 
            !appointment.getDatetime().equals(request.getDatetime())) {
            
            if (hasOverlap(request.getDoctorId(), request.getDatetime(), id)) {
                throw new RuntimeException("El doctor ya tiene una cita en ese horario");
            }
            
            // Validar que la fecha no sea pasada
            if (request.getDatetime().isBefore(LocalDateTime.now())) {
                throw new RuntimeException("No se puede reprogramar a una fecha pasada");
            }
        }
        
        // Actualizar doctor si cambió
        if (!appointment.getDoctor().getId().equals(request.getDoctorId())) {
            Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor no encontrado"));
            appointment.setDoctor(doctor);
        }
        
        // Actualizar paciente si cambió
        if (!appointment.getPatient().getId().equals(request.getPatientId())) {
            Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));
            appointment.setPatient(patient);
        }
        
        appointment.setDatetime(request.getDatetime());
        appointment.setNotes(request.getNotes());
        
        Appointment updated = appointmentRepository.save(appointment);
        
        return mapToResponse(updated);
    }
    
    /**
     * Cancelar una cita
     */
    @Transactional
    public AppointmentResponse cancelAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Cita no encontrada"));
        
        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new RuntimeException("La cita ya está cancelada");
        }
        
        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new RuntimeException("No se puede cancelar una cita completada");
        }
        
        appointment.setStatus(AppointmentStatus.CANCELLED);
        Appointment cancelled = appointmentRepository.save(appointment);
        
        return mapToResponse(cancelled);
    }
    
    /**
     * Confirmar una cita
     */
    @Transactional
    public AppointmentResponse confirmAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Cita no encontrada"));
        
        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new RuntimeException("No se puede confirmar una cita cancelada");
        }
        
        appointment.setStatus(AppointmentStatus.CONFIRMED);
        Appointment confirmed = appointmentRepository.save(appointment);
        
        return mapToResponse(confirmed);
    }
    
    /**
     * Completar una cita
     */
    @Transactional
    public AppointmentResponse completeAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Cita no encontrada"));
        
        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new RuntimeException("No se puede completar una cita cancelada");
        }
        
        appointment.setStatus(AppointmentStatus.COMPLETED);
        Appointment completed = appointmentRepository.save(appointment);
        
        return mapToResponse(completed);
    }
    
    /**
     * Validar si hay solapamiento de citas
     * Una cita dura 30 minutos por defecto
     */
    public boolean hasOverlap(Long doctorId, LocalDateTime datetime, Long excludeAppointmentId) {
        // Ventana de 30 minutos antes y después
        LocalDateTime start = datetime.minusMinutes(30);
        LocalDateTime end = datetime.plusMinutes(30);
        
        List<Appointment> overlapping = appointmentRepository
            .findOverlappingAppointments(doctorId, start, end, excludeAppointmentId);
        
        return !overlapping.isEmpty();
    }
    
    /**
     * Mapear entidad a DTO
     */
    private AppointmentResponse mapToResponse(Appointment appointment) {
        return AppointmentResponse.builder()
            .id(appointment.getId())
            .doctorId(appointment.getDoctor().getId())
            .doctorName("Doctor " + appointment.getDoctor().getId()) // Temporal hasta que User esté listo
            .specialty(appointment.getDoctor().getSpecialty())
            .patientId(appointment.getPatient().getId())
            .patientName("Paciente " + appointment.getPatient().getId()) // Temporal
            .datetime(appointment.getDatetime())
            .status(appointment.getStatus())
            .notes(appointment.getNotes())
            .qrCodeUrl(appointment.getQrCodeUrl())
            .createdAt(appointment.getCreatedAt())
            .updatedAt(appointment.getUpdatedAt())
            .build();
    }
}
