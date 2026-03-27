package io.github.artsobol.bookcrossing.feature.genre.dto;

import io.github.artsobol.bookcrossing.infrastructure.validation.annotation.NullOrNotBlank;
import io.github.artsobol.bookcrossing.infrastructure.validation.annotation.Slug;
import jakarta.validation.constraints.Size;

public record UpdateGenreRequest(
        @Size(max = 64, message = "genre.title.long")
        @NullOrNotBlank(message = "genre.title.blank")
        String title,

        String description,

        @Size(max = 128, message = "genre.slug.long")
        @Slug(message = "genre.slug.wrong")
        @NullOrNotBlank(message = "genre.slug.blank")
        String slug
) {
}
