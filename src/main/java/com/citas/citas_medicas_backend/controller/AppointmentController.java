package com.citas.citas_medicas_backend.controller;

import com.citas.citas_medicas_backend.dto.AppointmentRequest;
import com.citas.citas_medicas_backend.dto.AppointmentResponse;
import com.citas.citas_medicas_backend.model.AppointmentStatus;
import com.citas.citas_medicas_backend.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.PrintWriter;
import java.time.LocalDateTime;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    public ResponseEntity<List<AppointmentResponse>> getAllAppointments() {
        List<AppointmentResponse> appointments = appointmentService.getAllAppointments();
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO') or hasRole('PACIENTE')")
    public ResponseEntity<AppointmentResponse> getAppointmentById(@PathVariable @NonNull Long id) {
        Long appointmentId = Objects.requireNonNull(id, "id is required");
        AppointmentResponse appointment = appointmentService.getAppointmentById(appointmentId);
        return ResponseEntity.ok(appointment);
    }

    @GetMapping("/doctor/{doctorId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    public ResponseEntity<List<AppointmentResponse>> getAppointmentsByDoctor(@PathVariable @NonNull Long doctorId) {
        Long doctor = Objects.requireNonNull(doctorId, "doctorId is required");
        List<AppointmentResponse> appointments = appointmentService.getAppointmentsByDoctor(doctor);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PACIENTE')")
    public ResponseEntity<List<AppointmentResponse>> getAppointmentsByPatient(@PathVariable @NonNull Long patientId) {
        Long patient = Objects.requireNonNull(patientId, "patientId is required");
        List<AppointmentResponse> appointments = appointmentService.getAppointmentsByPatient(patient);
        return ResponseEntity.ok(appointments);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('PACIENTE')")
    public ResponseEntity<AppointmentResponse> createAppointment(@RequestBody @NonNull AppointmentRequest request) {
        AppointmentRequest safeRequest = Objects.requireNonNull(request, "request is required");
        AppointmentResponse response = appointmentService.createAppointment(safeRequest);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    public ResponseEntity<AppointmentResponse> updateAppointment(@PathVariable @NonNull Long id, @RequestBody @NonNull AppointmentRequest request) {
        Long appointmentId = Objects.requireNonNull(id, "id is required");
        AppointmentRequest safeRequest = Objects.requireNonNull(request, "request is required");
        AppointmentResponse response = appointmentService.updateAppointment(appointmentId, safeRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    public ResponseEntity<AppointmentResponse> cancelAppointment(@PathVariable @NonNull Long id) {
        Long appointmentId = Objects.requireNonNull(id, "id is required");
        AppointmentResponse response = appointmentService.cancelAppointment(appointmentId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/confirm")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    public ResponseEntity<AppointmentResponse> confirmAppointment(@PathVariable @NonNull Long id) {
        Long appointmentId = Objects.requireNonNull(id, "id is required");
        AppointmentResponse response = appointmentService.confirmAppointment(appointmentId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/complete")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    public ResponseEntity<AppointmentResponse> completeAppointment(@PathVariable @NonNull Long id) {
        Long appointmentId = Objects.requireNonNull(id, "id is required");
        AppointmentResponse response = appointmentService.completeAppointment(appointmentId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/filter")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    public ResponseEntity<List<AppointmentResponse>> getFilteredAppointments(
            @RequestParam(required = false) Long doctorId,
            @RequestParam(required = false) Long patientId,
            @RequestParam(required = false) AppointmentStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<AppointmentResponse> appointments = appointmentService.getFilteredAppointments(doctorId, patientId, status, startDate, endDate);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/export/csv")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    public void exportAppointmentsToCsv(HttpServletResponse response) throws Exception {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=appointments.csv");
        
        List<AppointmentResponse> appointments = appointmentService.getAllAppointments();
        
        try (PrintWriter writer = response.getWriter()) {
            writer.println("ID,Doctor ID,Doctor Name,Specialty,Patient ID,Patient Name,DateTime,Status,Notes,QR Code URL,Created At,Updated At");
            for (AppointmentResponse app : appointments) {
                writer.printf("%d,%d,%s,%s,%d,%s,%s,%s,%s,%s,%s,%s%n",
                    app.getId(),
                    app.getDoctorId(),
                    app.getDoctorName(),
                    app.getSpecialty(),
                    app.getPatientId(),
                    app.getPatientName(),
                    app.getDatetime().toString(),
                    app.getStatus().toString(),
                    app.getNotes() != null ? app.getNotes() : "",
                    app.getQrCodeUrl() != null ? app.getQrCodeUrl() : "",
                    app.getCreatedAt().toString(),
                    app.getUpdatedAt().toString());
            }
        }
    }

    private String safe(Object value) {
        return value == null ? "" : value.toString();
    }
}