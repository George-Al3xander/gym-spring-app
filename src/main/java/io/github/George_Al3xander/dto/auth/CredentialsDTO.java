package io.github.George_Al3xander.dto.auth;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CredentialsDTO {
    @NotEmpty
    private String username;

    @NotNull
    @Size(min = 10, max = 10)
    private String password;
}
