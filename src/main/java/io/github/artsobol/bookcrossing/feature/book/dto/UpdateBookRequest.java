package io.github.artsobol.bookcrossing.feature.book.dto;

import io.github.artsobol.bookcrossing.infrastructure.validation.annotation.NullOrNotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record UpdateBookRequest(
        @Size(max = 128, message = "book.title.long")
        @NullOrNotBlank(message = "book.title.blank")
        String title,

        String description,

        @Positive
        Long authorId,

        @Positive
        Long genreId
) {
}
