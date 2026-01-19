# 🧪 GUÍA DE PRUEBAS EN POSTMAN

## 📋 Configuración inicial
**Base URL:** `http://localhost:8080`

---

## 🔥 ORDEN DE PRUEBA RECOMENDADO

### 1️⃣ CREAR DOCTORES

**POST** `http://localhost:8080/api/doctors`

**Body (JSON):**
```json
{
  "userId": 1,
  "specialty": "Cardiología",
  "availability": "Lunes a Viernes 09:00-17:00"
}
```

**Respuesta esperada (201 Created):**
```json
{
  "id": 1,
  "userId": 1,
  "specialty": "Cardiología",
  "availability": "Lunes a Viernes 09:00-17:00"
}
```

**Crear otro doctor:**
```json
{
  "userId": 2,
  "specialty": "Pediatría",
  "availability": "Lunes, Miércoles, Viernes 10:00-14:00"
}
```

---

### 2️⃣ CREAR PACIENTES

**POST** `http://localhost:8080/api/patients`

**Body (JSON):**
```json
{
  "userId": 10,
  "insuranceNumber": "SEG-12345"
}
```

**Respuesta esperada (201 Created):**
```json
{
  "id": 1,
  "userId": 10,
  "insuranceNumber": "SEG-12345"
}
```

**Crear otro paciente:**
```json
{
  "userId": 11,
  "insuranceNumber": "SEG-67890"
}
```

---

### 3️⃣ CREAR CITAS

**POST** `http://localhost:8080/api/appointments`

**Body (JSON):**
```json
{
  "doctorId": 1,
  "patientId": 1,
  "datetime": "2026-01-20T10:00:00",
  "notes": "Primera consulta - dolor en el pecho"
}
```

**Respuesta esperada (201 Created):**
```json
{
  "id": 1,
  "doctorId": 1,
  "doctorName": "Doctor 1",
  "specialty": "Cardiología",
  "patientId": 1,
  "patientName": "Paciente 1",
  "datetime": "2026-01-20T10:00:00",
  "status": "CREATED",
  "notes": "Primera consulta - dolor en el pecho",
  "qrCodeUrl": null,
  "createdAt": "2026-01-15T18:35:00",
  "updatedAt": "2026-01-15T18:35:00"
}
```

**Crear cita que solapa (debe fallar):**
```json
{
  "doctorId": 1,
  "patientId": 2,
  "datetime": "2026-01-20T10:15:00",
  "notes": "Esta cita debe fallar por solapamiento"
}
```

**Respuesta esperada (400 Bad Request):**
```json
{
  "message": "El doctor ya tiene una cita en ese horario",
  "timestamp": "2026-01-15T18:36:00"
}
```

**Crear cita válida (diferente horario):**
```json
{
  "doctorId": 1,
  "patientId": 2,
  "datetime": "2026-01-20T11:00:00",
  "notes": "Segunda consulta del día"
}
```

---

### 4️⃣ LISTAR DATOS

**GET Todos los doctores**
`http://localhost:8080/api/doctors`

**GET Todos los pacientes**
`http://localhost:8080/api/patients`

**GET Todas las citas**
`http://localhost:8080/api/appointments`

**GET Doctor por ID**
`http://localhost:8080/api/doctors/1`

**GET Doctores por especialidad**
`http://localhost:8080/api/doctors/specialty/Cardiología`

**GET Citas por doctor**
`http://localhost:8080/api/appointments/doctor/1`

**GET Citas por paciente**
`http://localhost:8080/api/appointments/patient/1`

**GET Citas por estado**
`http://localhost:8080/api/appointments/status/CREATED`

---

### 5️⃣ ACTUALIZAR CITA

**PUT** `http://localhost:8080/api/appointments/1`

**Body (JSON):**
```json
{
  "doctorId": 1,
  "patientId": 1,
  "datetime": "2026-01-21T15:00:00",
  "notes": "Cita reprogramada - nueva fecha"
}
```

---

### 6️⃣ CAMBIAR ESTADO DE CITA

**PATCH Confirmar cita**
`http://localhost:8080/api/appointments/1/confirm`

**Respuesta:**
```json
{
  "id": 1,
  "status": "CONFIRMED",
  ...
}
```

**PATCH Completar cita**
`http://localhost:8080/api/appointments/1/complete`

**Respuesta:**
```json
{
  "id": 1,
  "status": "COMPLETED",
  ...
}
```

---

### 7️⃣ CANCELAR CITA

**DELETE** `http://localhost:8080/api/appointments/2`

**Respuesta:**
```json
{
  "id": 2,
  "status": "CANCELLED",
  ...
}
```

---

### 8️⃣ ACTUALIZAR DOCTOR

**PUT** `http://localhost:8080/api/doctors/1`

**Body:**
```json
{
  "userId": 1,
  "specialty": "Cardiología y Cirugía",
  "availability": "Lunes a Sábado 08:00-18:00"
}
```

---

### 9️⃣ ACTUALIZAR PACIENTE

**PUT** `http://localhost:8080/api/patients/1`

**Body:**
```json
{
  "userId": 10,
  "insuranceNumber": "SEG-12345-NEW"
}
```

---

## 🧪 CASOS DE PRUEBA ADICIONALES

### ❌ Pruebas de error

**Crear cita en el pasado (debe fallar):**
```json
POST /api/appointments
{
  "doctorId": 1,
  "patientId": 1,
  "datetime": "2025-12-01T10:00:00",
  "notes": "Fecha pasada"
}
```

**Crear cita con doctor inexistente (debe fallar):**
```json
POST /api/appointments
{
  "doctorId": 999,
  "patientId": 1,
  "datetime": "2026-01-25T10:00:00",
  "notes": "Doctor no existe"
}
```

**Paciente con número de seguro duplicado (debe fallar):**
```json
POST /api/patients
{
  "userId": 12,
  "insuranceNumber": "SEG-12345"
}
```

---

## 📦 COLECCIÓN POSTMAN (Importar)

Crea un archivo `Medical-API.postman_collection.json` con este contenido:

```json
{
  "info": {
    "name": "Medical Appointments API",
    "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
  },
  "item": [
    {
      "name": "Doctors",
      "item": [
        {
          "name": "Create Doctor",
          "request": {
            "method": "POST",
            "header": [{"key": "Content-Type", "value": "application/json"}],
            "body": {
              "mode": "raw",
              "raw": "{\n  \"userId\": 1,\n  \"specialty\": \"Cardiología\",\n  \"availability\": \"Lunes a Viernes 09:00-17:00\"\n}"
            },
            "url": "http://localhost:8080/api/doctors"
          }
        },
        {
          "name": "Get All Doctors",
          "request": {
            "method": "GET",
            "url": "http://localhost:8080/api/doctors"
          }
        }
      ]
    },
    {
      "name": "Patients",
      "item": [
        {
          "name": "Create Patient",
          "request": {
            "method": "POST",
            "header": [{"key": "Content-Type", "value": "application/json"}],
            "body": {
              "mode": "raw",
              "raw": "{\n  \"userId\": 10,\n  \"insuranceNumber\": \"SEG-12345\"\n}"
            },
            "url": "http://localhost:8080/api/patients"
          }
        },
        {
          "name": "Get All Patients",
          "request": {
            "method": "GET",
            "url": "http://localhost:8080/api/patients"
          }
        }
      ]
    },
    {
      "name": "Appointments",
      "item": [
        {
          "name": "Create Appointment",
          "request": {
            "method": "POST",
            "header": [{"key": "Content-Type", "value": "application/json"}],
            "body": {
              "mode": "raw",
              "raw": "{\n  \"doctorId\": 1,\n  \"patientId\": 1,\n  \"datetime\": \"2026-01-20T10:00:00\",\n  \"notes\": \"Primera consulta\"\n}"
            },
            "url": "http://localhost:8080/api/appointments"
          }
        },
        {
          "name": "Get All Appointments",
          "request": {
            "method": "GET",
            "url": "http://localhost:8080/api/appointments"
          }
        },
        {
          "name": "Confirm Appointment",
          "request": {
            "method": "PATCH",
            "url": "http://localhost:8080/api/appointments/1/confirm"
          }
        }
      ]
    }
  ]
}
```

---

## 🎯 FLUJO COMPLETO DE PRUEBA

1. ✅ Crear 2 doctores
2. ✅ Crear 2 pacientes
3. ✅ Listar doctores y pacientes
4. ✅ Crear cita válida
5. ❌ Intentar crear cita solapada (debe fallar)
6. ✅ Crear otra cita en horario diferente
7. ✅ Confirmar cita
8. ✅ Listar citas por doctor
9. ✅ Reprogramar cita
10. ✅ Completar cita
11. ✅ Cancelar una cita

---

## 🔍 Verificar en MySQL

```sql
-- Conéctate a MySQL
mysql -u app -p -h localhost -P 3307 medical_app

-- Ver doctores
SELECT * FROM doctors;

-- Ver pacientes
SELECT * FROM patients;

-- Ver citas
SELECT * FROM appointments;

-- Ver citas con detalles
SELECT 
  a.id,
  a.datetime,
  a.status,
  d.specialty,
  p.insurance_number
FROM appointments a
JOIN doctors d ON a.doctor_id = d.id
JOIN patients p ON a.patient_id = p.id;
```
