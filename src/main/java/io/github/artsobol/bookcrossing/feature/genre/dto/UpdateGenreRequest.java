package io.github.artsobol.bookcrossing.feature.genre.dto;

public record UpdateGenreRequest(
        String title,
        String description,
        String slug
) {
}
