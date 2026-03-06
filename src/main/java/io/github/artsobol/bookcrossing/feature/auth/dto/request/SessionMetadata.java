package io.github.artsobol.bookcrossing.feature.auth.dto.request;

public record SessionMetadata(
        String ipAddress,
        String userAgent,
        String deviceName
) {
}
