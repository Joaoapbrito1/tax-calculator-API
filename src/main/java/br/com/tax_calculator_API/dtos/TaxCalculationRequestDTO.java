package br.com.tax_calculator_API.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TaxCalculationRequestDTO {
    private Long taxTypeId;
    private Double valueBase;

}
