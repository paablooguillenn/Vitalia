package com.vitalia.appointment.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * Appointment entity representing medical appointments
 * TODO: Add id field (Long, auto-generated)
 * TODO: Add doctorId field (ManyToOne relationship with Doctor)
 * TODO: Add patientId field (ManyToOne relationship with Patient)
 * TODO: Add appointment date/time field
 * TODO: Add status field (enum: SCHEDULED, CONFIRMED, CANCELLED, COMPLETED)
 * TODO: Add reason/notes field
 * TODO: Add duration field
 * TODO: Add timestamps
 */
@Entity
@Table(name = "appointments")
public class Appointment {
    
    // TODO: Implement appointment entity fields
    
}
