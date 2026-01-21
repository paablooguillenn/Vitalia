package com.citas.citas_medicas_backend.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @After("execution(* com.citas.citas_medicas_backend.controller.AuthController.login(..))")
    public void logLogin(JoinPoint joinPoint) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            logger.info("Usuario {} inició sesión", auth.getName());
        }
    }

    @After("execution(* com.citas.citas_medicas_backend.controller.AuthController.register(..))")
    public void logRegister(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        if (args.length > 0 && args[0] instanceof com.citas.citas_medicas_backend.dto.AuthRequest request) {
            logger.info("Nuevo usuario registrado: {}", request.getEmail());
        }
    }

    @After("execution(* com.citas.citas_medicas_backend.service.AppointmentService.createAppointment(..))")
    public void logCreateAppointment(JoinPoint joinPoint) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            logger.info("Usuario {} creó una cita", auth.getName());
        }
    }

    // Más logs según necesidad
}