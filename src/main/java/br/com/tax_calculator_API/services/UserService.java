package br.com.tax_calculator_API.services;

import br.com.tax_calculator_API.dtos.UserRequestDTO;

public interface UserService {

    public void registerUser(UserRequestDTO userRequestDTO);
}