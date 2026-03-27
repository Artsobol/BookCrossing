package io.github.artsobol.bookcrossing.feature.author.dto;

import io.github.artsobol.bookcrossing.infrastructure.validation.annotation.Slug;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateAuthorRequest(
        @Size(max = 128, message = "author.name.long")
        @NotBlank(message = "author.name.blank")
        String name,

        @Size(max = 128, message = "author.slug.long")
        @Slug(message = "author.slug.wrong")
        @NotBlank(message = "author.slug.blank")
        String slug
) {
}
