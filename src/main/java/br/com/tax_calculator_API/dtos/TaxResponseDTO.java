package br.com.tax_calculator_API.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TaxResponseDTO {
   private Long id;
   private String name;
   private String description;
   private Double aliquot;
}
