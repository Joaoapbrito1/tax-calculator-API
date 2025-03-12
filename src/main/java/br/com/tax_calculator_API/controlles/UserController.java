package br.com.tax_calculator_API.controlles;

import br.com.tax_calculator_API.dtos.JwtResponseDTO;
import br.com.tax_calculator_API.dtos.UserLoginDTO;
import br.com.tax_calculator_API.services.AuthService;
import br.com.tax_calculator_API.dtos.UserRequestDTO;
import br.com.tax_calculator_API.services.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
    private AuthService authService;

    @PostMapping ("/register")
    public void registerUser(@RequestBody UserRequestDTO userRequestDTO){
        userServiceImpl.registerUser(userRequestDTO);
    }

    @PostMapping ("/login")
    public ResponseEntity<JwtResponseDTO> authenticateUser(@RequestBody UserLoginDTO userLoginDTO) {
        return ResponseEntity.ok(authService.login(userLoginDTO));
    }
}
