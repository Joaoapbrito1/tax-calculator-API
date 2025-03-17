package br.com.tax_calculator_API.services;

import br.com.tax_calculator_API.dtos.UserResponseDTO;
import br.com.tax_calculator_API.infra.jwt.JwtTokenProvider;
import br.com.tax_calculator_API.services.impl.AuthUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthUserServiceImpl implements AuthUserService {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public UserResponseDTO getUserInfo(String token) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Usuário não autenticado");
        }

        String username = authentication.getName();

        return new UserResponseDTO("Bem-vindo, " + username + "!");
    }

    @Override
    public String adminAccess() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_USER"))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acesso negado para usuários com o papel ROLE_USER");
        }

        return "Acesso admin";
    }
}