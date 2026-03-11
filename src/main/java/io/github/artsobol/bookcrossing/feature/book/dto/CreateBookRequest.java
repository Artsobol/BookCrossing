package io.github.artsobol.bookcrossing.feature.book.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateBookRequest(
        @NotBlank(message = "book.title.blank")
        String title,
        String description,
        Long authorId,
        Long genreId
) {
}
