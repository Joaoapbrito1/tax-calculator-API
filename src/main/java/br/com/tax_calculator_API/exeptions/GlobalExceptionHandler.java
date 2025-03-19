package br.com.tax_calculator_API.exeptions;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(UserNotAuthenticatedException.class)
    public ResponseEntity<ErrorResponse> handleUserNotAuthenticatedException(UserNotAuthenticatedException ex) {
        return buildResponse(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    @ExceptionHandler(UserInvalidDataException.class)
    public ResponseEntity<ErrorResponse> handleInvalidDataException(UserInvalidDataException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getReason());
    }

    @ExceptionHandler(UserResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(UserResourceNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getReason());
    }

    @ExceptionHandler(ResourceCreatedException.class)
    public ResponseEntity<ErrorResponse> handleResourceCreatedException(ResourceCreatedException ex) {
        return buildResponse(HttpStatus.CREATED, ex.getReason());
    }

    @ExceptionHandler(TaxNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTaxNotFoundException(TaxNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(TaxInvalidDataException.class)
    public ResponseEntity<ErrorResponse> handleInvalidTaxDataException(TaxInvalidDataException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, String message) {
        ErrorResponse errorResponse = new ErrorResponse(status.value(), message);
        return new ResponseEntity<>(errorResponse, status);
    }
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(404, ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.FORBIDDEN.value(), "User without permission, access restricted to Admin.");
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }
    @ExceptionHandler(TaxAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleTaxAlreadyExistsException(TaxAlreadyExistsException ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Object> handleResponseStatusException(ResponseStatusException ex) {
        return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception ex) {
        return ResponseEntity.status(500).body("Internal server error: " + ex.getMessage());
    }

}