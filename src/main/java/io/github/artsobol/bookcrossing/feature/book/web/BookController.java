package io.github.artsobol.bookcrossing.feature.book.web;

import io.github.artsobol.bookcrossing.feature.book.dto.BookResponse;
import io.github.artsobol.bookcrossing.feature.book.dto.CreateBookRequest;
import io.github.artsobol.bookcrossing.feature.book.dto.UpdateBookRequest;
import io.github.artsobol.bookcrossing.feature.book.service.BookService;
import io.github.artsobol.bookcrossing.security.user.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    @GetMapping
    public ResponseEntity<List<BookResponse>> getAllBooks() {
        log.debug("Received request to get all books");
        List<BookResponse> response = bookService.getAllBooks();
        log.debug("Return {} books", response.size());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<List<BookResponse>> getAllMyBooks(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        log.debug("Received request to get all own user books");
        List<BookResponse> response = bookService.getALlUserBooks(userPrincipal.userId());
        log.debug("Return {} user books", response.size());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<BookResponse>> getAllMyBooks(@PathVariable UUID userId) {
        log.debug("Received request to get all user books with id: {}", userId);
        List<BookResponse> response = bookService.getALlUserBooks(userId);
        log.debug("Return {} user books with id: {}", response.size(), userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getAllMyBooks(@PathVariable Long id) {
        log.debug("Received request to book with id: {}", id);
        BookResponse response = bookService.getById(id);
        log.debug("Return book with id: {}", id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<BookResponse> getAllMyBooks(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody @Valid CreateBookRequest request
    ) {
        log.debug("Received request to create book");
        BookResponse response = bookService.create(userPrincipal.userId(), request);
        log.debug("Return created book with id: {}", response.id());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BookResponse> getAllMyBooks(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody @Valid UpdateBookRequest request
    ) {
        log.debug("Received request update book with id: {}", id);
        BookResponse response = bookService.update(userPrincipal.userId(), id, request);
        log.debug("Return updated book with id: {}", id);
        return ResponseEntity.ok(response);
    }
}
