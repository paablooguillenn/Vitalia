package com.vitalia.demo.dto;

import com.vitalia.demo.entity.AppointmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentResponse {
    private Long id;
    private Long doctorId;
    private String doctorName;
    private String specialty;
    private Long patientId;
    private String patientName;
    private LocalDateTime datetime;
    private AppointmentStatus status;
    private String notes;
    private String qrCodeUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
