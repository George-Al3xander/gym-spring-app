package io.github.George_Al3xander.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeLoginRequest {
    @NotBlank
    private String username;

    @NotBlank
    private String oldPassword;

    @NotNull
    @Size(min = 10, max = 10)
    private String newPassword;
}
