package br.com.tax_calculator_API.exeptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ResourceCreatedException extends ResponseStatusException {
    public ResourceCreatedException(String message) {
        super(HttpStatus.CREATED, message);
    }
}
