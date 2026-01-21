package com.citas.citas_medicas_backend.service;

import com.citas.citas_medicas_backend.dto.AppointmentRequest;
import com.citas.citas_medicas_backend.dto.AppointmentResponse;
import com.citas.citas_medicas_backend.model.Appointment;
import com.citas.citas_medicas_backend.model.AppointmentStatus;
import com.citas.citas_medicas_backend.model.Role;
import com.citas.citas_medicas_backend.model.User;
import com.citas.citas_medicas_backend.repository.AppointmentRepository;
import com.citas.citas_medicas_backend.repository.UserRepository;
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
    private final UserRepository userRepository;
    
    /**
     * Crear una nueva cita
     */
    @Transactional
    public AppointmentResponse createAppointment(AppointmentRequest request) {
        // Validar que el doctor existe y tiene rol MEDICO
        User doctor = userRepository.findById(request.getDoctorId())
            .orElseThrow(() -> new RuntimeException("Doctor no encontrado"));
        
        if (doctor.getRole() != Role.MEDICO) {
            throw new RuntimeException("El usuario no tiene rol de doctor");
        }
        
        // Validar que el paciente existe y tiene rol PACIENTE
        User patient = userRepository.findById(request.getPatientId())
            .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));
        
        if (patient.getRole() != Role.PACIENTE) {
            throw new RuntimeException("El usuario no tiene rol de paciente");
        }
        
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
            User doctor = userRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor no encontrado"));
            if (doctor.getRole() != Role.MEDICO) {
                throw new RuntimeException("El usuario no tiene rol de doctor");
            }
            appointment.setDoctor(doctor);
        }
        
        // Actualizar paciente si cambió
        if (!appointment.getPatient().getId().equals(request.getPatientId())) {
            User patient = userRepository.findById(request.getPatientId())
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));
            if (patient.getRole() != Role.PACIENTE) {
                throw new RuntimeException("El usuario no tiene rol de paciente");
            }
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
     * Obtener citas filtradas
     */
    public List<AppointmentResponse> getFilteredAppointments(Long doctorId, Long patientId, AppointmentStatus status, LocalDateTime startDate, LocalDateTime endDate) {
        List<Appointment> appointments;
        if (doctorId != null && status != null && startDate != null && endDate != null) {
            appointments = appointmentRepository.findByDoctorIdAndStatusAndDatetimeBetween(doctorId, status, startDate, endDate);
        } else if (patientId != null && status != null && startDate != null && endDate != null) {
            appointments = appointmentRepository.findByPatientIdAndStatusAndDatetimeBetween(patientId, status, startDate, endDate);
        } else if (status != null && startDate != null && endDate != null) {
            appointments = appointmentRepository.findByStatusAndDatetimeBetween(status, startDate, endDate);
        } else if (doctorId != null) {
            appointments = appointmentRepository.findByDoctorId(doctorId);
        } else if (patientId != null) {
            appointments = appointmentRepository.findByPatientId(patientId);
        } else if (status != null) {
            appointments = appointmentRepository.findByStatus(status);
        } else {
            appointments = appointmentRepository.findAll();
        }
        return appointments.stream().map(this::mapToResponse).collect(Collectors.toList());
    }
    private AppointmentResponse mapToResponse(Appointment appointment) {
        User doctor = appointment.getDoctor();
        User patient = appointment.getPatient();
        
        String doctorName = doctor.getFirstName() + " " + doctor.getLastName();
        String patientName = patient.getFirstName() + " " + patient.getLastName();
        
        return AppointmentResponse.builder()
            .id(appointment.getId())
            .doctorId(doctor.getId())
            .doctorName(doctorName)
            .specialty(null) // No tenemos specialty en User
            .patientId(patient.getId())
            .patientName(patientName)
            .datetime(appointment.getDatetime())
            .status(appointment.getStatus())
            .notes(appointment.getNotes())
            .qrCodeUrl(appointment.getQrCodeUrl())
            .createdAt(appointment.getCreatedAt())
            .updatedAt(appointment.getUpdatedAt())
            .build();
    }
}
