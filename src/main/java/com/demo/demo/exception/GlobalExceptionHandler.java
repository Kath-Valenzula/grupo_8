package com.demo.demo.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja errores de validación de formularios
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleValidationException(MethodArgumentNotValidException ex, Model model) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> 
            errors.put(error.getField(), error.getDefaultMessage())
        );
        
        model.addAttribute("errors", errors);
        model.addAttribute("message", "Error de validación en el formulario");
        return "error";
    }

    /**
     * Maneja errores de acceso denegado (403 Forbidden)
     */
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleAccessDeniedException(AccessDeniedException ex, Model model, HttpServletRequest request) {
        model.addAttribute("status", HttpStatus.FORBIDDEN.value());
        model.addAttribute("error", "Acceso Denegado");
        model.addAttribute("message", "No tienes permisos para acceder a este recurso");
        model.addAttribute("path", request.getRequestURI());
        return "error";
    }

    /**
     * Maneja errores 404 - Recurso no encontrado
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFoundException(NoHandlerFoundException ex, Model model, HttpServletRequest request) {
        model.addAttribute("status", HttpStatus.NOT_FOUND.value());
        model.addAttribute("error", "Recurso No Encontrado");
        model.addAttribute("message", "La página que buscas no existe");
        model.addAttribute("path", request.getRequestURI());
        return "error";
    }

    /**
     * Maneja excepciones genéricas no capturadas
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleGlobalException(Exception ex, Model model, HttpServletRequest request) {
        model.addAttribute("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        model.addAttribute("error", "Error Interno del Servidor");
        model.addAttribute("message", "Ha ocurrido un error inesperado. Por favor, intenta nuevamente más tarde.");
        model.addAttribute("path", request.getRequestURI());
        
        // Log del error para debugging (no exponer al usuario)
        System.err.println("Error no manejado: " + ex.getMessage());
        ex.printStackTrace();
        
        return "error";
    }

    /**
     * Maneja excepciones de argumento ilegal
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleIllegalArgumentException(IllegalArgumentException ex, Model model, HttpServletRequest request) {
        model.addAttribute("status", HttpStatus.BAD_REQUEST.value());
        model.addAttribute("error", "Solicitud Inválida");
        model.addAttribute("message", ex.getMessage());
        model.addAttribute("path", request.getRequestURI());
        return "error";
    }
}
