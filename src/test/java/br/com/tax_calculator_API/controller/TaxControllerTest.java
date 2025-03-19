package br.com.tax_calculator_API.controller;

import br.com.tax_calculator_API.controlles.TaxController;
import br.com.tax_calculator_API.dtos.*;
import br.com.tax_calculator_API.models.TaxModel;
import br.com.tax_calculator_API.services.TaxService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaxControllerTest {

    @InjectMocks
    private TaxController taxController;

    @Mock
    private TaxService taxService;

    @BeforeEach
    void setUp() {

    }

    @Test
    void testCreateTaxes() {
        List<TaxRequestDTO> requestDTOs = Arrays.asList(new TaxRequestDTO(), new TaxRequestDTO());
        List<TaxResponseDTO> responseDTOs = Arrays.asList(new TaxResponseDTO(), new TaxResponseDTO());

        when(taxService.createTaxes(requestDTOs)).thenReturn(responseDTOs);

        ResponseEntity<List<TaxResponseDTO>> response = taxController.createTaxes(requestDTOs);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(2, Objects.requireNonNull(response.getBody()).size());
    }

    @Test
    void testGetAllTaxes() {
        List<TaxResponseDTO> responseDTOs = Arrays.asList(new TaxResponseDTO(), new TaxResponseDTO());
        when(taxService.allTaxes()).thenReturn(responseDTOs);

        ResponseEntity<List<TaxResponseDTO>> response = taxController.getAllTaxes();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, Objects.requireNonNull(response.getBody()).size());
    }

    @Test
    void testGetTaxById_Found() {
        Long id = 1L;
        TaxModel taxModel = new TaxModel();
        when(taxService.searchById(id)).thenReturn(Optional.of(taxModel));

        ResponseEntity<TaxModel> response = taxController.getTaxById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(taxModel, response.getBody());
    }

    @Test
    void testGetTaxById_NotFound() {
        Long id = 1L;
        when(taxService.searchById(id)).thenReturn(Optional.empty());

        ResponseEntity<TaxModel> response = taxController.getTaxById(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testDeleteTax() {
        Long id = 1L;
        doNothing().when(taxService).deleteTax(id);

        ResponseEntity<Void> response = taxController.deleteTax(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testUpdateTax() {
        Long id = 1L;
        TaxRequestDTO requestDTO = new TaxRequestDTO();
        TaxResponseDTO responseDTO = new TaxResponseDTO();

        when(taxService.updateTax(id, requestDTO)).thenReturn(responseDTO);

        ResponseEntity<TaxResponseDTO> response = taxController.updateTax(id, requestDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
    }

    @Test
    void testCalculateTax() {
        TaxCalculationRequestDTO requestDTO = new TaxCalculationRequestDTO();
        TaxCalculationResponseDTO responseDTO = new TaxCalculationResponseDTO();

        when(taxService.calculateTax(requestDTO)).thenReturn(responseDTO);

        ResponseEntity<TaxCalculationResponseDTO> response = taxController.calculateTax(requestDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
    }

}

