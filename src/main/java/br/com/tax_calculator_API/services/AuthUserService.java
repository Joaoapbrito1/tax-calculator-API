package br.com.tax_calculator_API.services;

import br.com.tax_calculator_API.dtos.UserResponseDTO;
import br.com.tax_calculator_API.exeptions.UserNotAuthenticatedException;

public interface AuthUserService {
    UserResponseDTO getUserInfo(String token) throws UserNotAuthenticatedException;
    String adminAccess();
}
