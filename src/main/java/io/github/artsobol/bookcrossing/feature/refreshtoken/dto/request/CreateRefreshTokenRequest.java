package io.github.artsobol.bookcrossing.feature.refreshtoken.dto.request;

import io.github.artsobol.bookcrossing.feature.user.entity.User;

import java.util.UUID;

public record CreateRefreshTokenRequest(
        User user,
        UUID sessionId,
        String ipAddress,
        String userAgent,
        String deviceName
) {
}
