package com.vitalia.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorRequest {
    private Long userId;
    private String specialty;
    private String availability; // JSON string con horarios
}
