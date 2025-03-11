package br.com.tax_calculator_API.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserLoginDTO {
    private String username;
    private String password;
}
