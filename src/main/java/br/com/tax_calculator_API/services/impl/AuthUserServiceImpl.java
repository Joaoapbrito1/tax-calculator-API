package br.com.tax_calculator_API.services.impl;

import br.com.tax_calculator_API.dtos.UserResponseDTO;
import br.com.tax_calculator_API.exeptions.InvalidDataException;
import br.com.tax_calculator_API.exeptions.ResourceNotFoundException;
import br.com.tax_calculator_API.exeptions.UserNotAuthenticatedException;
import br.com.tax_calculator_API.infra.jwt.JwtTokenProvider;
import br.com.tax_calculator_API.services.AuthUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthUserServiceImpl implements AuthUserService {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public UserResponseDTO getUserInfo(String token) throws UserNotAuthenticatedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UserNotAuthenticatedException("Usuário não autenticado");
        }
        String username = authentication.getName();
        return new UserResponseDTO("Bem-vindo, " + username + "!");
    }

    @Override
    public String adminAccess() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_USER"))) {
            throw new InvalidDataException("Acesso negado para usuários com o papel ROLE_USER");
        }
        return "Acesso admin";
    }

    public void deleteResource(Long resourceId) {
        boolean resourceExists = false;
        if (!resourceExists) {
            throw new ResourceNotFoundException("Recurso não encontrado");
        }
    }
}