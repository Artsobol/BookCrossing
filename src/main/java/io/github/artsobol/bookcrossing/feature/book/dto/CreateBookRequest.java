package io.github.artsobol.bookcrossing.feature.book.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record CreateBookRequest(
        @Size(max = 128, message = "book.title.long")
        @NotBlank(message = "book.title.blank")
        String title,

        String description,

        @Positive
        Long authorId,

        @Positive
        Long genreId
) {
}
