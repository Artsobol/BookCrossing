package io.github.artsobol.bookcrossing.feature.profile.dto.request;

import io.github.artsobol.bookcrossing.infrastructure.validation.annotation.NullOrNotBlank;
import jakarta.validation.constraints.Size;

public record UpdateProfileRequest(
        @Size(max = 32, message = "profile.firstName.long")
        @NullOrNotBlank(message = "profile.firstName.blank")
        String firstName,

        @Size(max = 32, message = "profile.lastName.long")
        @NullOrNotBlank(message = "profile.lastName.blank")
        String lastName,

        String bio
) {
}
