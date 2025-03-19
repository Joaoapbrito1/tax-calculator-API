package br.com.tax_calculator_API.exeptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UserResourceNotFoundException extends ResponseStatusException {
    public UserResourceNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
