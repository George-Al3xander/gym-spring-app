package io.github.George_Al3xander.controller;

import io.github.George_Al3xander.auth.JwtUtil;
import io.github.George_Al3xander.dto.auth.ChangeLoginRequest;
import io.github.George_Al3xander.dto.auth.CredentialsDTO;
import io.github.George_Al3xander.dto.auth.LoginResponse;
import io.github.George_Al3xander.service.AuthenticationService;
import io.github.George_Al3xander.service.BruteForceProtectionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(
        name = "Authentication",
        description = "Authentication and credential management operations"
)
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final JwtUtil jwtUtil;
    private final BruteForceProtectionService bruteForceProtectionService;

    @PostMapping("/login")
    @Operation(
            summary = "Authenticate user",
            description = "Validates user credentials and returns JWT token"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Authentication successful"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authentication failed"
            )
    })
    public ResponseEntity<LoginResponse> login(
            @Parameter(description = "User credentials", required = true)
            @Valid @RequestBody CredentialsDTO credentials,
            HttpServletRequest httpRequest
    ) {
        String key =
                credentials.getUsername() + ":" +
                        httpRequest.getRemoteAddr();


        if (bruteForceProtectionService.isBlocked(key)) {

            return ResponseEntity
                    .status(HttpStatus.TOO_MANY_REQUESTS)
                    .build();
        }

        boolean authenticated =
                authenticationService.authenticate(credentials);

        if (!authenticated) {
            bruteForceProtectionService.loginFailed(key);
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .build();
        } else {
            bruteForceProtectionService.loginSucceeded(key);
        }

        return ResponseEntity.ok(
                new LoginResponse(
                        jwtUtil.generateToken(credentials.getUsername())
                )
        );
    }


    @PutMapping("/change-password")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(
            summary = "Change password",
            description = "Changes password for the authenticated user"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Password successfully changed"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized request"
            )
    })
    public ResponseEntity<Void> changeLogin(
            @Parameter(
                    description = "New password request",
                    required = true
            )
            @Valid @RequestBody ChangeLoginRequest request
    ) {

        authenticationService.changePassword(request);

        return ResponseEntity.ok().build();
    }
}