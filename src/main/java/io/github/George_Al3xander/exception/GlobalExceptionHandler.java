package io.github.George_Al3xander.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<Map<String, String>> handleValidationException(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public ResponseEntity<Map<String, String>> handleInvalidJson() {

        Map<String, String> error = new HashMap<>();
        error.put("error", "Request body is missing or invalid JSON");

        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler({EntityInUseException.class, EntityNotFoundException.class, ActivationStateConflictException.class})
    @ResponseBody
    public ResponseEntity<Map<String, String>> handleCustomEntityExceptions(Exception ex) {

        Map<String, String> error = new HashMap<>();
        error.put("message", ex.getMessage());

        HttpStatus status = ex instanceof EntityInUseException ? HttpStatus.CONFLICT : HttpStatus.NOT_FOUND;

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseBody
    public ResponseEntity<Map<String, String>> handleInvalidCredentials() {

        Map<String, String> error = new HashMap<>();
        error.put("error", "Invalid username or password.");

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<Map<String, String>> handleException(Exception ex) {

        Map<String, String> error = new HashMap<>();
        error.put("error", "An unexpected error occurred. Please try again later.");

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
