package io.github.artsobol.bookcrossing.infrastructure.web.error.dto;

public record ValidationFieldError(
        String field,
        String message
) {
}
