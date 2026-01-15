package com.vitalia.demo.service;

import com.vitalia.demo.dto.PatientRequest;
import com.vitalia.demo.dto.PatientResponse;
import com.vitalia.demo.entity.Patient;
import com.vitalia.demo.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientService {
    
    private final PatientRepository patientRepository;
    
    @Transactional
    public PatientResponse createPatient(PatientRequest request) {
        // Validar que no exista otro paciente con el mismo número de seguro
        if (request.getInsuranceNumber() != null) {
            patientRepository.findByInsuranceNumber(request.getInsuranceNumber())
                .ifPresent(p -> {
                    throw new RuntimeException("Ya existe un paciente con ese número de seguro");
                });
        }
        
        Patient patient = new Patient();
        patient.setUserId(request.getUserId());
        patient.setInsuranceNumber(request.getInsuranceNumber());
        
        Patient saved = patientRepository.save(patient);
        return mapToResponse(saved);
    }
    
    public List<PatientResponse> getAllPatients() {
        return patientRepository.findAll().stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }
    
    public PatientResponse getPatientById(Long id) {
        Patient patient = patientRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));
        return mapToResponse(patient);
    }
    
    @Transactional
    public PatientResponse updatePatient(Long id, PatientRequest request) {
        Patient patient = patientRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));
        
        // Validar número de seguro duplicado
        if (request.getInsuranceNumber() != null && 
            !request.getInsuranceNumber().equals(patient.getInsuranceNumber())) {
            patientRepository.findByInsuranceNumber(request.getInsuranceNumber())
                .ifPresent(p -> {
                    throw new RuntimeException("Ya existe un paciente con ese número de seguro");
                });
        }
        
        patient.setUserId(request.getUserId());
        patient.setInsuranceNumber(request.getInsuranceNumber());
        
        Patient updated = patientRepository.save(patient);
        return mapToResponse(updated);
    }
    
    @Transactional
    public void deletePatient(Long id) {
        if (!patientRepository.existsById(id)) {
            throw new RuntimeException("Paciente no encontrado");
        }
        patientRepository.deleteById(id);
    }
    
    private PatientResponse mapToResponse(Patient patient) {
        return PatientResponse.builder()
            .id(patient.getId())
            .userId(patient.getUserId())
            .insuranceNumber(patient.getInsuranceNumber())
            .build();
    }
}
