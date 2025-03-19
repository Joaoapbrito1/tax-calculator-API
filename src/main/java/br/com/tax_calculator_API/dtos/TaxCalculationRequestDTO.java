package br.com.tax_calculator_API.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaxCalculationRequestDTO {
    private Long taxTypeId;
    private Double valueBase;

}