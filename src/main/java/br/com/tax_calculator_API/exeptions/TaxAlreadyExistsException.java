package br.com.tax_calculator_API.exeptions;

public class TaxAlreadyExistsException extends RuntimeException {

    public TaxAlreadyExistsException(String message) {
        super(message);
    }
}
