package br.com.tax_calculator_API.dtos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JwtResponseDTO {
    private String token;

    public JwtResponseDTO(String token) {
        this.token = token;
    }

}