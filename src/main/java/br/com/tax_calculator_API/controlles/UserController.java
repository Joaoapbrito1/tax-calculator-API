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
@Tag(name = "Users", description = "User management")
@SecurityRequirement(name = SwaggerConfig.SECURITY)
public class UserController {

    private final UserServiceImpl userServiceImpl;
    private final AuthService authService;

    @Operation(
            summary = "Register user.",
            description = "Method to register a new user",
            responses = {
                    @ApiResponse(responseCode = "201", description = "User successfully registered."),
                    @ApiResponse(responseCode = "400", description = "User already registered.", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Internal server error.", content = @Content)
            }
    )
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserRequestDTO userRequestDTO) {
        try {
            userServiceImpl.registerUser(userRequestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("User successfully registered.");
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User already registered.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error registering user.");
        }
    }

    @Operation(
            summary = "User login.",
            description = "Method for user authentication.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User successfully logged in.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = JwtResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "User does not exist.", content = @Content)
            }
    )
    @PostMapping("/login")
    public ResponseEntity<JwtResponseDTO> login(@RequestBody UserLoginDTO userLoginDTO) {
        JwtResponseDTO jwtResponseDTO = authService.login(userLoginDTO);
        return new ResponseEntity<>(jwtResponseDTO, HttpStatus.OK);
    }
}