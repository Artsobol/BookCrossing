package io.github.artsobol.bookcrossing.feature.author.dto;

import io.github.artsobol.bookcrossing.infrastructure.validation.annotation.NullOrNotBlank;
import io.github.artsobol.bookcrossing.infrastructure.validation.annotation.Slug;
import jakarta.validation.constraints.Size;

public record UpdateAuthorRequest(
        @Size(max = 128, message = "author.name.long")
        @NullOrNotBlank(message = "author.name.blank")
        String name,

        @Size(max = 128, message = "author.slug.long")
        @Slug(message = "author.slug.wrong")
        @NullOrNotBlank(message = "author.slug.blank")
        String slug
) {
}
