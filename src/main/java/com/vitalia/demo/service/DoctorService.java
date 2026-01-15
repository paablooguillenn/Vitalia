package com.vitalia.demo.service;

import com.vitalia.demo.dto.DoctorRequest;
import com.vitalia.demo.dto.DoctorResponse;
import com.vitalia.demo.entity.Doctor;
import com.vitalia.demo.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorService {
    
    private final DoctorRepository doctorRepository;
    
    @Transactional
    public DoctorResponse createDoctor(DoctorRequest request) {
        Doctor doctor = new Doctor();
        doctor.setUserId(request.getUserId());
        doctor.setSpecialty(request.getSpecialty());
        doctor.setAvailability(request.getAvailability());
        
        Doctor saved = doctorRepository.save(doctor);
        return mapToResponse(saved);
    }
    
    public List<DoctorResponse> getAllDoctors() {
        return doctorRepository.findAll().stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }
    
    public DoctorResponse getDoctorById(Long id) {
        Doctor doctor = doctorRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Doctor no encontrado"));
        return mapToResponse(doctor);
    }
    
    public List<DoctorResponse> getDoctorsBySpecialty(String specialty) {
        return doctorRepository.findBySpecialty(specialty).stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }
    
    @Transactional
    public DoctorResponse updateDoctor(Long id, DoctorRequest request) {
        Doctor doctor = doctorRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Doctor no encontrado"));
        
        doctor.setUserId(request.getUserId());
        doctor.setSpecialty(request.getSpecialty());
        doctor.setAvailability(request.getAvailability());
        
        Doctor updated = doctorRepository.save(doctor);
        return mapToResponse(updated);
    }
    
    @Transactional
    public void deleteDoctor(Long id) {
        if (!doctorRepository.existsById(id)) {
            throw new RuntimeException("Doctor no encontrado");
        }
        doctorRepository.deleteById(id);
    }
    
    private DoctorResponse mapToResponse(Doctor doctor) {
        return DoctorResponse.builder()
            .id(doctor.getId())
            .userId(doctor.getUserId())
            .specialty(doctor.getSpecialty())
            .availability(doctor.getAvailability())
            .build();
    }
}
