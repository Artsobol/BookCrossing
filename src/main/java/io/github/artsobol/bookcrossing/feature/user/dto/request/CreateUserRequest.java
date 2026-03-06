package io.github.artsobol.bookcrossing.feature.user.dto.request;

public record CreateUserRequest(
        String username,
        String email,
        String passwordHash
) {
}
