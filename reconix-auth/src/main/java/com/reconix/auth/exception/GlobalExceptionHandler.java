package com.reconix.auth.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.time.Instant;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(AuthenticationFailedException.class)
    public ProblemDetail handleAuthenticationFailed(AuthenticationFailedException ex) {
        log.warn("[EXCEPTION] AuthenticationFailed: {}", ex.getMessage());
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.UNAUTHORIZED, ex.getMessage());
        problem.setTitle("Falha na autenticacao");
        problem.setType(URI.create("https://reconix.com/errors/authentication-failed"));
        problem.setProperty("timestamp", Instant.now());
        problem.setProperty("service", "reconix-auth");
        return problem;
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ProblemDetail handleUserNotFound(UserNotFoundException ex) {
        log.warn("[EXCEPTION] UserNotFound: {}", ex.getMessage());
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND, ex.getMessage());
        problem.setTitle("Usuario nao encontrado");
        problem.setType(URI.create("https://reconix.com/errors/user-not-found"));
        problem.setProperty("timestamp", Instant.now());
        problem.setProperty("service", "reconix-auth");
        return problem;
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ProblemDetail handleUserAlreadyExists(UserAlreadyExistsException ex) {
        log.warn("[EXCEPTION] UserAlreadyExists: {}", ex.getMessage());
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT, ex.getMessage());
        problem.setTitle("Usuario ja existe");
        problem.setType(URI.create("https://reconix.com/errors/user-already-exists"));
        problem.setProperty("timestamp", Instant.now());
        problem.setProperty("service", "reconix-auth");
        return problem;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex) {
        String errors = ex.getBindingResult().getFieldErrors().stream()
                .map(f -> f.getField() + ": " + f.getDefaultMessage())
                .collect(Collectors.joining(", "));

        log.warn("[EXCEPTION] Validation: {}", errors);
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, errors);
        problem.setTitle("Erro de validacao");
        problem.setType(URI.create("https://reconix.com/errors/validation-error"));
        problem.setProperty("timestamp", Instant.now());
        problem.setProperty("service", "reconix-auth");
        return problem;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneral(Exception ex) {
        log.error("[EXCEPTION] Erro inesperado: {}", ex.getMessage(), ex);
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno do servidor");
        problem.setTitle("Erro interno");
        problem.setType(URI.create("https://reconix.com/errors/internal-error"));
        problem.setProperty("timestamp", Instant.now());
        problem.setProperty("service", "reconix-auth");
        return problem;
    }
}