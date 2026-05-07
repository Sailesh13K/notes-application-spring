package com.sailesh.notes.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ResponseStatusException.class)
  public ResponseEntity<ApiResponse<String>> handleResponseStatus(ResponseStatusException ex) {
    return ResponseEntity
      .status(ex.getStatusCode())
      .body(ApiResponse.fail(ex.getReason()));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiResponse<String>> handleValidation(MethodArgumentNotValidException ex) {
    FieldError error = ex.getBindingResult().getFieldErrors().stream().findFirst().orElse(null);
    String message = error == null ? "Invalid request" : error.getField() + " " + error.getDefaultMessage();
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.fail(message));
  }

  @ExceptionHandler(AuthenticationException.class)
  public ResponseEntity<ApiResponse<String>> handleAuthentication(AuthenticationException ex) {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.fail("Invalid credentials"));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiResponse<String>> handleUnexpected(Exception ex) {
    return ResponseEntity
      .status(HttpStatus.INTERNAL_SERVER_ERROR)
      .body(ApiResponse.fail("Something went wrong"));
  }
}
