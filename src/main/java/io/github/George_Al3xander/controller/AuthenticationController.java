package io.github.George_Al3xander.controller;

import io.github.George_Al3xander.auth.AuthHttpHeader;
import io.github.George_Al3xander.dto.auth.ChangeLoginRequest;
import io.github.George_Al3xander.dto.auth.CredentialsDTO;
import io.github.George_Al3xander.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(
        name = "Authentication",
        description = "Authentication and credential management operations"
)
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Authenticate user",
            description = "Validates user credentials and authenticates the user"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Authentication successful"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid credentials format"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authentication failed"
            )
    })
    public ResponseEntity<Void> login(
            @Parameter(
                    description = "Username",
                    required = true
            )
            @RequestParam String username,

            @Parameter(
                    description = "Password",
                    required = true
            )
            @RequestParam String password
    ) {
        boolean authenticated = authenticationService.authenticate(
                new CredentialsDTO(username, password)
        );

        if (!authenticated) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok().build();
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
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
                    responseCode = "400",
                    description = "Invalid password format"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized request"
            )
    })
    public void changeLogin(
            @Parameter(
                    name = AuthHttpHeader.USERNAME,
                    description = "Authenticated username",
                    required = true,
                    in = ParameterIn.HEADER
            )
            @RequestHeader(AuthHttpHeader.USERNAME) String username,

            @Parameter(
                    description = "New password request",
                    required = true
            )
            @Valid @RequestBody ChangeLoginRequest request
    ) {
        authenticationService.changePassword(username, request);
    }
}