package io.github.artsobol.bookcrossing.feature.auth.dto.response;

import io.github.artsobol.bookcrossing.feature.role.entity.Role;

import java.util.Set;
import java.util.UUID;

public record UserInfo(
        UUID userId,
        String username,
        Set<Role> roles
) {
}
