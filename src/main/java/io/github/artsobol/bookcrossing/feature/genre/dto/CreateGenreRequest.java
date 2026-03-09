package io.github.artsobol.bookcrossing.feature.genre.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateGenreRequest(
        @NotBlank(message = "genre.title.blank")
        String title,
        String description,
        @NotBlank(message = "genre.slug.blank")
        String slug
) {
}
