package br.com.tax_calculator_API.dtos;

import br.com.tax_calculator_API.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class UserRequestDTO {
    private String username;
    private String password;
    private Set<UserRole> roles;

}
