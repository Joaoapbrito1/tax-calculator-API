package br.com.tax_calculator_API.exeptions;

public class TaxInvalidDataException extends RuntimeException {
    public TaxInvalidDataException(String message) {
        super(message);
    }
}