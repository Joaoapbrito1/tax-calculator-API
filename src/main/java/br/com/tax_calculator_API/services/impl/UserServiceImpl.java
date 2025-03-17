package br.com.tax_calculator_API.services;


import br.com.tax_calculator_API.dtos.UserRequestDTO;
import br.com.tax_calculator_API.models.UserModel;
import br.com.tax_calculator_API.models.UserRoleModel;
import br.com.tax_calculator_API.repository.UserRepository;
import br.com.tax_calculator_API.repository.UserRoleRepository;
import br.com.tax_calculator_API.services.impl.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserRoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public void registerUser(UserRequestDTO userRequestDTO) {
        if (userRepository.existsByUsername(userRequestDTO.getUsername())) {
            throw new RuntimeException("Unprocess Entity");
        }

        UserModel user = new UserModel();
        user.setUsername(userRequestDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));

        Set<UserRoleModel> roles = userRequestDTO.getRoles().stream()
                .map(r -> new UserRoleModel(r.name()))
                .collect(Collectors.toSet());
        roleRepository.saveAll(roles);
        user.setUserRoleModels(roles);
        userRepository.save(user);
    }
}