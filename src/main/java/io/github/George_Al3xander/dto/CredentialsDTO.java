package io.github.George_Al3xander.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class CredentialsDTO {
    private final String username;
    private final String password;
}
