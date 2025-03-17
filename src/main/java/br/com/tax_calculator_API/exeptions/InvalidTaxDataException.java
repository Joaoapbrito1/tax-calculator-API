package br.com.tax_calculator_API.exeptions;

public class InvalidTaxDataException extends RuntimeException {
    public InvalidTaxDataException(String message) {
        super(message);
    }
}