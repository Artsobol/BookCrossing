package io.github.artsobol.bookcrossing.infrastructure.web.error.dto;

import java.time.Instant;

public record ErrorResponse(
        Instant timestamp,
        int status,
        String error,
        String errorCode,
        String message,
        String path
) {
}
