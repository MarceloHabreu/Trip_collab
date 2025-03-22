package io.github.marcelohabreu.tripCollab.exceptions;

import io.github.marcelohabreu.tripCollab.exceptions.user.CustomBadCredentialsException;
import io.github.marcelohabreu.tripCollab.exceptions.user.EmailAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // constant formater for timestamp
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss").withZone(ZoneId.systemDefault());

    // method to get the timestamp
    public String getCurrentTimestamp() {
        return FORMATTER.format(Instant.now());
    }

    // Validations
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> response = new HashMap<>();
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        response.put("errors", errors);
        response.put("timestamp", getCurrentTimestamp());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // User
    @ExceptionHandler(CustomBadCredentialsException.class)
    public ResponseEntity<Map<String, Object>> handleBadCredentialsException(CustomBadCredentialsException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", ex.getMessage());
        response.put("timestamp", getCurrentTimestamp());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleEmailAlreadyExistyException(EmailAlreadyExistsException ex, WebRequest request) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", ex.getMessage());
        response.put("timestamp", getCurrentTimestamp());
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
    }

    // Generics
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "An unexpected error occurred.");
        response.put("details", ex.getMessage());
        response.put("timestamp", getCurrentTimestamp());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

}
