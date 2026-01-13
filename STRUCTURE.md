# Vitalia - Medical Appointment Manager Backend

## Overview
This is the initial backend structure for the Vitalia Medical Appointment Manager, built with Spring Boot, Maven, and following best practices for a medical appointment management system.

## Technology Stack
- **Java 17**
- **Spring Boot 3.2.1**
- **Spring Security** with JWT authentication
- **Spring Data JPA** with Hibernate
- **PostgreSQL** database
- **Flyway** for database migrations
- **WebSocket** for real-time notifications
- **Maven** for build management
- **Lombok** for reducing boilerplate code

## Project Structure

```
src/
├── main/
│   ├── java/com/vitalia/
│   │   ├── VitaliaApplication.java          # Main application entry point
│   │   ├── config/                          # Configuration classes
│   │   │   ├── SecurityConfig.java          # Spring Security configuration
│   │   │   ├── JwtConfig.java               # JWT token configuration
│   │   │   ├── CorsConfig.java              # CORS configuration
│   │   │   └── WebSocketConfig.java         # WebSocket configuration
│   │   ├── auth/                            # Authentication module
│   │   │   ├── controller/
│   │   │   ├── service/
│   │   │   ├── dto/
│   │   │   └── filter/
│   │   ├── user/                            # User management module
│   │   │   ├── controller/
│   │   │   ├── service/
│   │   │   ├── repository/
│   │   │   ├── model/
│   │   │   └── dto/
│   │   ├── doctor/                          # Doctor management module
│   │   │   ├── controller/
│   │   │   ├── service/
│   │   │   ├── repository/
│   │   │   ├── model/
│   │   │   └── dto/
│   │   ├── patient/                         # Patient management module
│   │   │   ├── controller/
│   │   │   ├── service/
│   │   │   ├── repository/
│   │   │   ├── model/
│   │   │   └── dto/
│   │   ├── appointment/                     # Appointment management module
│   │   │   ├── controller/
│   │   │   ├── service/
│   │   │   ├── repository/
│   │   │   ├── model/
│   │   │   └── dto/
│   │   ├── file/                            # File handling module
│   │   │   ├── controller/
│   │   │   ├── service/
│   │   │   ├── model/
│   │   │   └── dto/
│   │   ├── notification/                    # Notification system module
│   │   │   ├── controller/
│   │   │   ├── service/
│   │   │   ├── model/
│   │   │   └── dto/
│   │   ├── qr/                              # QR code generation module
│   │   │   ├── controller/
│   │   │   ├── service/
│   │   │   └── dto/
│   │   ├── scheduler/                       # Scheduled tasks
│   │   │   ├── AppointmentReminderScheduler.java
│   │   │   └── CleanupScheduler.java
│   │   └── stats/                           # Statistics and analytics module
│   │       ├── controller/
│   │       ├── service/
│   │       └── dto/
│   └── resources/
│       ├── application.yml                  # Application configuration
│       └── db/migration/                    # Flyway migration scripts
│           └── V1__Initial_Schema.sql
└── test/
    └── java/com/vitalia/
        └── VitaliaApplicationTests.java     # Basic test class
```

## Package Overview

### config/
Configuration classes for Spring Security, JWT, CORS, and WebSocket.

### auth/
Authentication and authorization logic including login, registration, and JWT token management.

### user/
User management including user CRUD operations and user profiles.

### doctor/
Doctor profile management, specializations, and availability scheduling.

### patient/
Patient profile management, medical history, and insurance information.

### appointment/
Appointment scheduling, management, and availability checking.

### file/
File upload, download, and management for medical documents and records.

### notification/
Notification system supporting email, SMS, push, and in-app notifications.

### qr/
QR code generation and validation for appointment check-ins.

### scheduler/
Scheduled tasks for appointment reminders and system cleanup.

### stats/
Statistics and analytics for dashboard and reporting.

## Configuration

The `application.yml` file contains configuration for:
- Database connection (PostgreSQL)
- JPA/Hibernate settings
- Flyway migrations
- JWT configuration
- File upload settings
- CORS settings
- Logging levels

**Important:** Update the following before deployment:
- Database credentials
- JWT secret key
- CORS allowed origins
- File storage directory

## Building and Running

### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher
- PostgreSQL 12 or higher

### Build
```bash
mvn clean install
```

### Run
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080/api`

## Database Setup

1. Create a PostgreSQL database:
```sql
CREATE DATABASE vitalia;
CREATE USER vitalia_user WITH PASSWORD 'your_password';
GRANT ALL PRIVILEGES ON DATABASE vitalia TO vitalia_user;
```

2. Update `application.yml` with your database credentials.

3. Flyway will automatically run migrations on startup.

## API Documentation

Once implemented, the API will be available at:
- Base URL: `http://localhost:8080/api`
- Authentication endpoints: `/api/auth/*`
- User endpoints: `/api/users/*`
- Doctor endpoints: `/api/doctors/*`
- Patient endpoints: `/api/patients/*`
- Appointment endpoints: `/api/appointments/*`
- File endpoints: `/api/files/*`
- Notification endpoints: `/api/notifications/*`
- QR endpoints: `/api/qr/*`
- Statistics endpoints: `/api/stats/*`

## Next Steps

All classes currently contain TODO comments indicating the implementation needed. Key areas to implement:

1. **Security Configuration**: Configure Spring Security with JWT authentication
2. **Entity Definitions**: Complete JPA entity classes with proper relationships
3. **Repository Methods**: Add custom query methods to repositories
4. **Service Logic**: Implement business logic in service classes
5. **Controller Endpoints**: Define REST API endpoints
6. **Database Migrations**: Complete Flyway migration scripts
7. **Tests**: Add comprehensive unit and integration tests

## Development Guidelines

- Follow Spring Boot best practices
- Use DTOs for API requests/responses
- Implement proper error handling
- Add input validation
- Write comprehensive tests
- Document APIs with Swagger/OpenAPI
- Follow SOLID principles
- Keep security as top priority

## License

[Add your license information here]

## Contributors

[Add contributor information here]
