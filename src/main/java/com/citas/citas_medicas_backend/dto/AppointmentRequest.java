package com.citas.citas_medicas_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentRequest {
    private Long doctorId;
    private Long patientId;
    private LocalDateTime datetime;
    private String notes;
}
