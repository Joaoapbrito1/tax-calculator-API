package br.com.tax_calculator_API.controlles;

import br.com.tax_calculator_API.dtos.TaxCalculationRequestDTO;
import br.com.tax_calculator_API.dtos.TaxCalculationResponseDTO;
import br.com.tax_calculator_API.dtos.TaxRequestDTO;
import br.com.tax_calculator_API.dtos.TaxResponseDTO;
import br.com.tax_calculator_API.infra.swegger.SwaggerConfig;
import br.com.tax_calculator_API.models.TaxModel;
import br.com.tax_calculator_API.services.TaxService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/api/tipos")
@Tag(name = "Taxes", description = "Tax management")
@SecurityRequirement(name = SwaggerConfig.SECURITY)
public class TaxController {

    @Autowired
    private final TaxService taxService;

    @Operation(
            summary = "Create taxes.",
            description = "Method to create a list of taxes.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Tax successfully created."),
                    @ApiResponse(responseCode = "400", description = "Tax already registered.", content = @Content),
                    @ApiResponse(responseCode = "403", description = "User without permission, access restricted to Admin."),
                    @ApiResponse(responseCode = "500", description = "Internal server error.", content = @Content)
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<List<TaxResponseDTO>> createTaxes(@RequestBody List<TaxRequestDTO> taxRequestDTOs) {
        List<TaxResponseDTO> createdTaxes = taxService.createTaxes(taxRequestDTOs);
        return new ResponseEntity<>(createdTaxes, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Retrieve all taxes.",
            description = "Method to list all registered taxes.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Tax list successfully retrieved."),
                    @ApiResponse(responseCode = "500", description = "Internal server error.", content = @Content)
            }
    )
    @GetMapping
    public ResponseEntity<List<TaxResponseDTO>> getAllTaxes() {
        List<TaxResponseDTO> taxes = taxService.allTaxes();
        return new ResponseEntity<>(taxes, HttpStatus.OK);
    }

    @Operation(
            summary = "Retrieve tax by ID.",
            description = "Method to retrieve a tax by ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Tax successfully found."),
                    @ApiResponse(responseCode = "404", description = "Tax not found.", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Internal server error.", content = @Content)
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<TaxModel> getTaxById(@PathVariable Long id) {
        Optional<TaxModel> tax = taxService.searchById(id);
        return tax.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(
            summary = "Delete tax by ID.",
            description = "Method to delete a tax by ID.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Tax successfully deleted."),
                    @ApiResponse(responseCode = "403", description = "User without permission, access restricted to Admin."),
                    @ApiResponse(responseCode = "404", description = "Tax not found.", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Internal server error.", content = @Content)
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTax(@PathVariable Long id) {
        taxService.deleteTax(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Update tax by ID.",
            description = "Method to update a tax by ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Tax successfully updated."),
                    @ApiResponse(responseCode = "404", description = "Tax not found.", content = @Content),
                    @ApiResponse(responseCode = "403", description = "User without permission, access restricted to Admin."),
                    @ApiResponse(responseCode = "500", description = "Internal server error.", content = @Content)
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<TaxResponseDTO> updateTax(@PathVariable Long id, @RequestBody @Valid TaxRequestDTO taxRequestDTO) {
        TaxResponseDTO updatedTax = taxService.updateTax(id, taxRequestDTO);
        return ResponseEntity.ok(updatedTax);
    }

    @Operation(
            summary = "Calculate tax.",
            description = "Method to calculate the tax amount based on the base value.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Calculation successfully performed."),
                    @ApiResponse(responseCode = "403", description = "User without permission, access restricted to Admin."),
                    @ApiResponse(responseCode = "404", description = "Tax not found.", content = @Content),
                    @ApiResponse(responseCode = "400", description = "Invalid data.", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Internal server error.", content = @Content)
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/calculo")
    public ResponseEntity<TaxCalculationResponseDTO> calculateTax(@RequestBody @Valid TaxCalculationRequestDTO requestDTO) {
        TaxCalculationResponseDTO responseDTO = taxService.calculateTax(requestDTO);
        return ResponseEntity.ok(responseDTO);
    }
}