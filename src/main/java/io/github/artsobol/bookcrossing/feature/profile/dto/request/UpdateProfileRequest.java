package io.github.artsobol.bookcrossing.feature.profile.dto.request;

public record UpdateProfileRequest(
        String firstName,
        String lastName,
        String bio
) {
}
