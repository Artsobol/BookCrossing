package io.github.artsobol.bookcrossing.feature.refreshtoken.dto.request;

public record RotateRefreshTokenRequest(
        String rawRefreshToken,
        String ipAddress,
        String userAgent
) {
}
