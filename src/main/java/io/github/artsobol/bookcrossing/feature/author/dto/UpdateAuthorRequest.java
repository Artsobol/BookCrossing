package io.github.artsobol.bookcrossing.feature.author.dto;

public record UpdateAuthorRequest(
        String name,
        String slug
) {
}
