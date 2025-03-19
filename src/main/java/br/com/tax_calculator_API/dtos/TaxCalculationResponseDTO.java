package br.com.tax_calculator_API.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaxCalculationResponseDTO {
   private String taxType;
   private Double baseValue;
   private Double aliquot;
   private Double valueTax;
}
