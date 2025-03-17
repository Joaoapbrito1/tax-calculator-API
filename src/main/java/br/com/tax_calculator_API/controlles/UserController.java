package br.com.tax_calculator_API.controlles;

import br.com.tax_calculator_API.dtos.JwtResponseDTO;
import br.com.tax_calculator_API.dtos.UserLoginDTO;
import br.com.tax_calculator_API.dtos.UserRequestDTO;
import br.com.tax_calculator_API.exeptions.UserAlreadyExistsException;
import br.com.tax_calculator_API.infra.swegger.SwaggerConfig;
import br.com.tax_calculator_API.services.AuthService;
import br.com.tax_calculator_API.services.impl.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Tag(name = "Usuarios", description = "Controle de usuarios")
@SecurityRequirement(name = SwaggerConfig.SECURITY)
public class UserController {

    private final UserServiceImpl userServiceImpl;
    private final AuthService authService;

    @PostMapping("/register")
    @Operation(
            summary = "Registra usuario.",
            description = "Método para cadastrar um usuario",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Usuario cadastrado com sucesso."),
                    @ApiResponse(responseCode = "400", description = "Usuario ja cadastrado.", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Erro interno no servidor.", content = @Content)
            }
    )
    public ResponseEntity<String> registerUser(@RequestBody UserRequestDTO userRequestDTO) {
        try {
            userServiceImpl.registerUser(userRequestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("Usuario cadastrado com sucesso.");
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuario ja cadastrado.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao cadastrar usuario.");
        }
    }

    @PostMapping("/login")
    @Operation(
            summary = "Login usuario.",
            description = "Método para o usuario logar.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuario logado com sucesso.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = JwtResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Esse usuario não existe.", content = @Content)
            }
    )
    public ResponseEntity<JwtResponseDTO> login(@RequestBody UserLoginDTO userLoginDTO) {
        JwtResponseDTO jwtResponseDTO = authService.login(userLoginDTO);
        return new ResponseEntity<>(jwtResponseDTO, HttpStatus.OK);
    }
}