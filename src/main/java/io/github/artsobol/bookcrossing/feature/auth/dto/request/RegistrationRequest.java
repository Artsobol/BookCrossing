package io.github.artsobol.bookcrossing.feature.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegistrationRequest(
        @NotBlank(message = "auth.user.username.blank")
        String username,
        @Email(message = "auth.user.email.invalid")
        @NotBlank(message = "auth.user.email.blank")
        String email,
        @NotBlank(message = "auth.user.password.blank")
        String password,
        @NotBlank(message = "auth.user.password.confirm.blank")
        String confirmPassword
) {
}
