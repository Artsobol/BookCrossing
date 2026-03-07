package io.github.artsobol.bookcrossing.feature.user.dto.response;

import io.github.artsobol.bookcrossing.feature.role.dto.response.RoleResponse;

import java.util.Set;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String username,
        Set<RoleResponse> roles
) {
}
