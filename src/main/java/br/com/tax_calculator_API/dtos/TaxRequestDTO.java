package br.com.tax_calculator_API.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaxRequestDTO {
   private String name;
   private String description;
   private Double aliquot;
}
