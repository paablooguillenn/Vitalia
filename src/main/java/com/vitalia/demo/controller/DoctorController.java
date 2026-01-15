package com.vitalia.demo.controller;

import com.vitalia.demo.dto.DoctorRequest;
import com.vitalia.demo.dto.DoctorResponse;
import com.vitalia.demo.dto.ErrorResponse;
import com.vitalia.demo.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
public class DoctorController {
    
    private final DoctorService doctorService;
    
    /**
     * Crear un nuevo doctor
     * POST /api/doctors
     */
    @PostMapping
    public ResponseEntity<?> createDoctor(@RequestBody DoctorRequest request) {
        try {
            DoctorResponse response = doctorService.createDoctor(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }
    
    /**
     * Obtener todos los doctores
     * GET /api/doctors
     */
    @GetMapping
    public ResponseEntity<List<DoctorResponse>> getAllDoctors() {
        List<DoctorResponse> doctors = doctorService.getAllDoctors();
        return ResponseEntity.ok(doctors);
    }
    
    /**
     * Obtener doctor por ID
     * GET /api/doctors/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getDoctorById(@PathVariable Long id) {
        try {
            DoctorResponse response = doctorService.getDoctorById(id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(e.getMessage()));
        }
    }
    
    /**
     * Buscar doctores por especialidad
     * GET /api/doctors/specialty/{specialty}
     */
    @GetMapping("/specialty/{specialty}")
    public ResponseEntity<List<DoctorResponse>> getDoctorsBySpecialty(@PathVariable String specialty) {
        List<DoctorResponse> doctors = doctorService.getDoctorsBySpecialty(specialty);
        return ResponseEntity.ok(doctors);
    }
    
    /**
     * Actualizar doctor
     * PUT /api/doctors/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateDoctor(
            @PathVariable Long id,
            @RequestBody DoctorRequest request) {
        try {
            DoctorResponse response = doctorService.updateDoctor(id, request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }
    
    /**
     * Eliminar doctor
     * DELETE /api/doctors/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDoctor(@PathVariable Long id) {
        try {
            doctorService.deleteDoctor(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(e.getMessage()));
        }
    }
}
