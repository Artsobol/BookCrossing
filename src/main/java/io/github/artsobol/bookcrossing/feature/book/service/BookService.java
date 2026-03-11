package io.github.artsobol.bookcrossing.feature.book.service;

import io.github.artsobol.bookcrossing.feature.book.dto.BookResponse;
import io.github.artsobol.bookcrossing.feature.book.dto.CreateBookRequest;
import io.github.artsobol.bookcrossing.feature.book.dto.UpdateBookRequest;

import java.util.List;
import java.util.UUID;

public interface BookService {

    BookResponse create(UUID userId, CreateBookRequest request);

    List<BookResponse> getAllBooks();

    List<BookResponse> getALlUserBooks(UUID userId);

    BookResponse getById(Long id);

    BookResponse update(UUID userId, Long id, UpdateBookRequest request);
}
