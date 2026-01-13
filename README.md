# Vitalia

Backend para sistema de gestión de citas médicas construido con Java Spring Boot.

## Estructura del Proyecto

Este proyecto contiene la estructura inicial completa para un gestor de citas médicas con las siguientes características:

### Tecnologías
- **Java 17**
- **Spring Boot 3.2.1**
- **Spring Security** con autenticación JWT
- **Spring Data JPA** con Hibernate
- **PostgreSQL**
- **Flyway** para migraciones de base de datos
- **WebSocket** para notificaciones en tiempo real
- **Maven** para gestión de dependencias

### Paquetes Implementados

1. **config/** - Configuración de seguridad, JWT, CORS y WebSocket
2. **auth/** - Sistema de autenticación y JWT
3. **user/** - Gestión de usuarios
4. **doctor/** - Gestión de médicos
5. **patient/** - Gestión de pacientes
6. **appointment/** - Sistema de citas médicas
7. **file/** - Manejo de archivos
8. **notification/** - Sistema de notificaciones
9. **qr/** - Generación de códigos QR
10. **scheduler/** - Tareas programadas
11. **stats/** - Estadísticas y analíticas

### Estado Actual

✅ Estructura de directorios creada  
✅ 47 archivos Java con clases vacías y comentarios TODO  
✅ Configuración de ejemplo (application.yml)  
✅ Directorio de migraciones Flyway  
✅ .gitignore configurado  
✅ Proyecto compila correctamente  
✅ Tests básicos funcionando  

### Siguiente Pasos

Consulta el archivo `STRUCTURE.md` para más detalles sobre la arquitectura del proyecto y las implementaciones pendientes.

### Construir y Ejecutar

```bash
# Compilar
mvn clean install

# Ejecutar
mvn spring-boot:run
```

**Nota:** Necesitarás configurar PostgreSQL y actualizar las credenciales en `application.yml` antes de ejecutar la aplicación.