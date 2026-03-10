package io.github.artsobol.bookcrossing.feature.author.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateAuthorRequest(
        @NotBlank(message = "author.name.blank")
        String name,
        @NotBlank(message = "author.slug.blank")
        String slug
) {
}
