package br.com.tax_calculator_API.exeptions;

public class UserNotAuthenticatedException extends Throwable {
    public UserNotAuthenticatedException(String message) {
        super(message);
    }
}