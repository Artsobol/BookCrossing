package io.github.artsobol.bookcrossing.feature.profile.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateProfileRequest(
        @Size(max = 32, message = "profile.firstName.long")
        @NotBlank(message = "profile.firstName.blank")
        String firstName,

        @Size(max = 32, message = "profile.lastName.long")
        @NotBlank(message = "profile.lastName.blank")
        String lastName,

        String bio
) {
}
