package io.github.artsobol.bookcrossing.feature.profile.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateProfileRequest(
        @NotBlank(message = "profile.firstName.blank")
        String firstName,
        @NotBlank(message = "profile.lastName.blank")
        String lastName,
        String bio
) {
}
