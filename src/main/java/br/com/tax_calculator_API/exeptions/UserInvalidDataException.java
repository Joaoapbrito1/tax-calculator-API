package br.com.tax_calculator_API.exeptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UserInvalidDataException extends ResponseStatusException {
    public UserInvalidDataException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
