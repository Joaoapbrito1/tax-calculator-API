package br.com.tax_calculator_API.services;

import br.com.tax_calculator_API.dtos.TaxCalculationRequestDTO;
import br.com.tax_calculator_API.dtos.TaxCalculationResponseDTO;
import br.com.tax_calculator_API.dtos.TaxRequestDTO;
import br.com.tax_calculator_API.dtos.TaxResponseDTO;
import br.com.tax_calculator_API.models.TaxModel;

import java.util.List;
import java.util.Optional;


public interface TaxService {
    List<TaxResponseDTO> createTaxes(List<TaxRequestDTO> taxRequestDTOs);
    List<TaxResponseDTO> allTaxes();
    Optional<TaxModel> searchById(Long id);
    void deleteTax(Long id);
    TaxResponseDTO updateTax(Long id, TaxRequestDTO taxRequestDTO);
    TaxCalculationResponseDTO calculateTax(TaxCalculationRequestDTO requestDTO);
}
