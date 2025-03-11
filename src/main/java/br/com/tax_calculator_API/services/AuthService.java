package br.com.tax_calculator_API.services;


import br.com.tax_calculator_API.dtos.JwtResponseDTO;
import br.com.tax_calculator_API.dtos.UserLoginDTO;

public interface AuthService {
    JwtResponseDTO login(UserLoginDTO userLoginDTO);

}