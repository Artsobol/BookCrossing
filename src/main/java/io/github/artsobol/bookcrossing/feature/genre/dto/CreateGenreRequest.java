package io.github.artsobol.bookcrossing.feature.genre.dto;

import io.github.artsobol.bookcrossing.infrastructure.validation.annotation.Slug;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateGenreRequest(
        @Size(max = 64, message = "genre.title.long")
        @NotBlank(message = "genre.title.blank")
        String title,

        String description,

        @Size(max = 128, message = "genre.slug.long")
        @Slug(message = "genre.slug.wrong")
        @NotBlank(message = "genre.slug.blank")
        String slug
) {
}
