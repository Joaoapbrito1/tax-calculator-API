package br.com.tax_calculator_API.exeptions;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class GlobalExceptionHandler{

    @ExceptionHandler(UserNotAuthenticatedException.class)
    public ResponseEntity<String> handleUserNotAuthenticatedException(UserNotAuthenticatedException ex) {
        return ResponseEntity.status(401).body(ex.getMessage());
    }

    @ExceptionHandler(InvalidDataException.class)
    public ResponseEntity<String> handleInvalidDataException(InvalidDataException ex) {
        return ResponseEntity.status(400).body(ex.getReason());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return ResponseEntity.status(404).body(ex.getReason());
    }

    @ExceptionHandler(ResourceCreatedException.class)
    public ResponseEntity<String> handleResourceCreatedException(ResourceCreatedException ex) {
        return ResponseEntity.status(201).body(ex.getReason());
    }
    @ExceptionHandler(TaxNotFoundException.class)
    public ResponseEntity<String> handleTaxNotFoundException(TaxNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(InvalidTaxDataException.class)
    public ResponseEntity<String> handleInvalidTaxDataException(InvalidTaxDataException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno no servidor: " + ex.getMessage());
    }

}