package br.com.tax_calculator_API.services;

import br.com.tax_calculator_API.dtos.UserResponseDTO;

public interface AuthUserService {
    UserResponseDTO getUserInfo(String token);
    String adminAccess();
}
