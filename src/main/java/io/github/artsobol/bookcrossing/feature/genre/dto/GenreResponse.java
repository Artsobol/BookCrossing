package io.github.artsobol.bookcrossing.feature.genre.dto;

public record GenreResponse(
        Long id,
        String title,
        String description,
        String slug
) {
}
