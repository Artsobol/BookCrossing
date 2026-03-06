package io.github.artsobol.bookcrossing.feature.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "auth.user.username.blank")
        String username,
        @NotBlank(message = "auth.user.password.blank")
        String password
) {
}
