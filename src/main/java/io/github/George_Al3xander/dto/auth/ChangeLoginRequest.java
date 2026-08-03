package io.github.George_Al3xander.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeLoginRequest {
    @NotNull
    private String oldPassword;

    @NotNull
    @Size(min = 10, max = 10)
    private String newPassword;
}
