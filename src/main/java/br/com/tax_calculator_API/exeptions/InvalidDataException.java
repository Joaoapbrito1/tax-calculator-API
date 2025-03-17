package br.com.tax_calculator_API.exeptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InvalidDataException extends ResponseStatusException {
    public InvalidDataException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
