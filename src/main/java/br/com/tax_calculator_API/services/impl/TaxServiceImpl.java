package br.com.tax_calculator_API.services.impl;

import br.com.tax_calculator_API.dtos.*;
import br.com.tax_calculator_API.exeptions.TaxAlreadyExistsException;
import br.com.tax_calculator_API.exeptions.TaxInvalidDataException;
import br.com.tax_calculator_API.exeptions.TaxNotFoundException;
import br.com.tax_calculator_API.models.TaxModel;
import br.com.tax_calculator_API.repository.TaxRepository;
import br.com.tax_calculator_API.services.TaxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaxServiceImpl implements TaxService {

    private final TaxRepository taxRepository;

    @Autowired
    public TaxServiceImpl(TaxRepository taxRepository) {
        this.taxRepository = taxRepository;
    }

    @Override
    public List<TaxResponseDTO> createTaxes(List<TaxRequestDTO> taxRequestDTOs) {
        if (taxRequestDTOs.isEmpty()) {
            throw new TaxInvalidDataException("The tax list cannot be empty.");
        }

        List<TaxModel> taxModels = taxRequestDTOs.stream()
                .map(this::mapToTaxModel)
                .collect(Collectors.toList());

        for (TaxModel taxModel : taxModels) {
            if (taxRepository.findByName(taxModel.getName()).isPresent()) {
                throw new TaxAlreadyExistsException("Tax already registered.");
            }
        }

        List<TaxModel> savedTaxes = taxRepository.saveAll(taxModels);

        return savedTaxes.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaxResponseDTO> allTaxes() {
        return taxRepository.findAll().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<TaxModel> searchById(Long id) {
        return taxRepository.findById(id);
    }

    @Override
    public void deleteTax(Long id) {
        if (!taxRepository.existsById(id)) {
            throw new TaxNotFoundException("Tax not found with ID " + id);
        }
        taxRepository.deleteById(id);
    }

    @Override
    public TaxResponseDTO updateTax(Long id, TaxRequestDTO taxRequestDTO) {
        TaxModel existingTaxModel = taxRepository.findById(id)
                .orElseThrow(() -> new TaxNotFoundException("Tax not found with ID: " + id));

        existingTaxModel.setName(taxRequestDTO.getName());
        existingTaxModel.setDescription(taxRequestDTO.getDescription());
        existingTaxModel.setAliquot(taxRequestDTO.getAliquot());

        TaxModel updatedTaxModel = taxRepository.save(existingTaxModel);
        return convertToResponseDTO(updatedTaxModel);
    }

    public TaxModel mapToTaxModel(TaxRequestDTO taxRequestDTO) {
        TaxModel taxModel = new TaxModel();
        taxModel.setName(taxRequestDTO.getName());
        taxModel.setDescription(taxRequestDTO.getDescription());
        taxModel.setAliquot(taxRequestDTO.getAliquot());
        return taxModel;
    }

    private TaxResponseDTO convertToResponseDTO(TaxModel taxModel) {
        TaxResponseDTO responseDTO = new TaxResponseDTO();
        responseDTO.setId(taxModel.getId());
        responseDTO.setName(taxModel.getName());
        responseDTO.setDescription(taxModel.getDescription());
        responseDTO.setAliquot(taxModel.getAliquot());
        return responseDTO;
    }
    @Override
    public TaxCalculationResponseDTO calculateTax(TaxCalculationRequestDTO taxCalculationRequestDTO) {
        TaxModel taxModel = taxRepository.findById(taxCalculationRequestDTO.getTaxTypeId())
                .orElseThrow(() -> new TaxNotFoundException("Tax not found with ID " + taxCalculationRequestDTO.getTaxTypeId()));

        Double valueTax = (taxModel.getAliquot() / 100) * taxCalculationRequestDTO.getValueBase();

        TaxCalculationResponseDTO responseDTO = new TaxCalculationResponseDTO();
        responseDTO.setTaxType(taxModel.getName());
        responseDTO.setBaseValue(taxCalculationRequestDTO.getValueBase());
        responseDTO.setAliquot(taxModel.getAliquot());
        responseDTO.setValueTax(valueTax);

        return responseDTO;
    }
}