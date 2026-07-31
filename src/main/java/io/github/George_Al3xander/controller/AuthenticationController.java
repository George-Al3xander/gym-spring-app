package io.github.George_Al3xander.controller;

import io.github.George_Al3xander.dto.auth.ChangeLoginRequest;
import io.github.George_Al3xander.dto.auth.CredentialsDTO;
import io.github.George_Al3xander.service.AuthenticationService;
import io.github.George_Al3xander.web.AuthHttpHeader;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/auth")
@RequiredArgsConstructor
@Api(tags = "Authentication", description = "Authentication and credential management operations")
public class AuthenticationController {

    private final AuthenticationService authenticationService;


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(
            value = "Authenticate user",
            notes = "Validates user credentials and authenticates the user"
    )
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Authentication successful"
            ),
            @ApiResponse(
                    code = 400,
                    message = "Invalid credentials format"
            ),
            @ApiResponse(
                    code = 401,
                    message = "Authentication failed"
            )
    })
    public ResponseEntity<Void> login(
            @ApiParam(
                    value = "User credentials",
                    required = true
            )
            @RequestParam String username,
            @RequestParam String password
    ) {
        boolean authenticated = authenticationService.authenticate(new CredentialsDTO(username, password));

        if (!authenticated) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok().build();
    }


    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(
            value = "Change password",
            notes = "Changes password for authenticated user"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = AuthHttpHeader.USERNAME,
                    value = "Authenticated username",
                    required = true,
                    paramType = "header",
                    dataType = "string"
            ),
            @ApiImplicitParam(
                    name = AuthHttpHeader.PASSWORD,
                    value = "Current user password",
                    required = true,
                    paramType = "header",
                    dataType = "string"
            )
    })
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Password successfully changed"
            ),
            @ApiResponse(
                    code = 400,
                    message = "Invalid password format"
            ),
            @ApiResponse(
                    code = 401,
                    message = "Unauthorized request"
            )
    })
    public void changeLogin(
            @RequestHeader(AuthHttpHeader.USERNAME) String username,

            @ApiParam(
                    value = "New password request",
                    required = true
            )
            @Valid @RequestBody ChangeLoginRequest request
    ) {
        authenticationService.changePassword(
                username,
                request
        );
    }

}