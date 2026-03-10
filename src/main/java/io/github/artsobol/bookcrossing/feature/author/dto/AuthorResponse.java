package io.github.artsobol.bookcrossing.feature.author.dto;

public record AuthorResponse(
        Long id,
        String name,
        String slug
) {
}
