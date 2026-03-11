package io.github.artsobol.bookcrossing.feature.book.dto;

public record UpdateBookRequest(
        String title,
        String description,
        Long authorId,
        Long genreId
) {
}
