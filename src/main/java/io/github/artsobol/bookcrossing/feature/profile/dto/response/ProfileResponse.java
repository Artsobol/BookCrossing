package io.github.artsobol.bookcrossing.feature.profile.dto.response;

import io.github.artsobol.bookcrossing.feature.user.dto.response.UserResponse;

public record ProfileResponse(
        Long id,
        String firstName,
        String lastName,
        String bio,
        UserResponse user
) {
}
