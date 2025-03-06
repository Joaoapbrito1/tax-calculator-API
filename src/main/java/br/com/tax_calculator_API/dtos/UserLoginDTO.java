package br.com.tax_calculator_API.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserLoginDTO {
    private String username;
    private String password;
}
