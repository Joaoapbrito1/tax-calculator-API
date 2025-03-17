package br.com.tax_calculator_API.services.impl;

import br.com.tax_calculator_API.dtos.JwtResponseDTO;
import br.com.tax_calculator_API.dtos.UserLoginDTO;
import br.com.tax_calculator_API.infra.jwt.JwtTokenProvider;
import br.com.tax_calculator_API.repository.UserRepository;
import br.com.tax_calculator_API.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @Override
    public JwtResponseDTO login(UserLoginDTO userLoginDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userLoginDTO.getUsername(),
                        userLoginDTO.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenProvider.generateToken(authentication);

        return new JwtResponseDTO(token);
    }
}