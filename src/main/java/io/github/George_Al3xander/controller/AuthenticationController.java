package io.github.George_Al3xander.controller;

import io.github.George_Al3xander.dto.auth.ChangeLoginRequest;
import io.github.George_Al3xander.dto.auth.CredentialsDTO;
import io.github.George_Al3xander.service.AuthenticationService;
import io.github.George_Al3xander.web.AuthHttpHeader;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public void login(
            @Valid @RequestBody CredentialsDTO credentialsDTO
    ) {
        authenticationService.authenticate(credentialsDTO);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public void changeLogin(
            @RequestHeader(AuthHttpHeader.USERNAME) String username,
            @Valid @RequestBody ChangeLoginRequest request
    ) {
        authenticationService.changePassword(
                new CredentialsDTO(username, request.getNewPassword())
        );
    }

}
