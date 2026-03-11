package io.github.artsobol.bookcrossing.feature.book.dto;

import io.github.artsobol.bookcrossing.feature.author.dto.AuthorResponse;
import io.github.artsobol.bookcrossing.feature.book.entity.BookStatus;
import io.github.artsobol.bookcrossing.feature.genre.dto.GenreResponse;
import io.github.artsobol.bookcrossing.feature.user.dto.response.UserResponse;

public record BookResponse(
        Long id,
        String title,
        String description,
        BookStatus status,
        AuthorResponse author,
        GenreResponse genre,
        UserResponse user
) {
}
