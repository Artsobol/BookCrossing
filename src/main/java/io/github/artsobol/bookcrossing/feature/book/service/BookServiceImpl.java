package io.github.artsobol.bookcrossing.feature.book.service;

import io.github.artsobol.bookcrossing.exception.http.ForbiddenException;
import io.github.artsobol.bookcrossing.exception.http.NotFoundException;
import io.github.artsobol.bookcrossing.feature.author.service.AuthorFinder;
import io.github.artsobol.bookcrossing.feature.book.dto.BookResponse;
import io.github.artsobol.bookcrossing.feature.book.dto.CreateBookRequest;
import io.github.artsobol.bookcrossing.feature.book.dto.UpdateBookRequest;
import io.github.artsobol.bookcrossing.feature.book.entity.Book;
import io.github.artsobol.bookcrossing.feature.book.mapper.BookMapper;
import io.github.artsobol.bookcrossing.feature.book.repository.BookRepository;
import io.github.artsobol.bookcrossing.feature.genre.service.GenreFinder;
import io.github.artsobol.bookcrossing.feature.user.entity.User;
import io.github.artsobol.bookcrossing.feature.user.service.UserFinder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@Transactional
@PreAuthorize("hasAnyAuthority('USER')")
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final UserFinder userFinder;
    private final AuthorFinder authorFinder;
    private final GenreFinder genreFinder;
    private final BookMapper bookMapper;

    @Override
    public BookResponse create(UUID userId, CreateBookRequest request) {
        log.info("Start creating book");
        Book entity = bookMapper.toEntity(request);
        User user = userFinder.findById(userId);
        entity.setUser(user);
        if (request.authorId() != null) {
            entity.setAuthor(authorFinder.findById(request.authorId()));
            log.debug("Author with id: {} successfully added to book", request.authorId());
        }
        if (request.genreId() != null) {
            entity.setGenre(genreFinder.findById(request.genreId()));
            log.debug("Genre with id: {} successfully added to book", request.genreId());
        }
        Book saved = bookRepository.save(entity);
        log.info("Book successfully created with id {}", saved.getId());
        return bookMapper.toResponse(saved);
    }

    @Override
    public List<BookResponse> getAllBooks() {
        log.debug("Finding all books");
        List<BookResponse> response = bookRepository.findAll().stream().map(bookMapper::toResponse).toList();
        log.debug("Find {} books", response.size());
        return response;
    }

    @Override
    public List<BookResponse> getALlUserBooks(UUID userId) {
        log.debug("Finding all user books with id: {}", userId);
        List<BookResponse> response = bookRepository.findAllByUserId(userId)
                .stream()
                .map(bookMapper::toResponse)
                .toList();
        log.debug("Find {} user books", response.size());
        return response;
    }

    @Override
    public BookResponse getById(Long id) {
        return bookMapper.toResponse(findBookById(id));
    }

    @Override
    public BookResponse update(UUID userId, Long id, UpdateBookRequest request) {
        log.info("Start updating book with id: {}", id);
        ensureCorrectOwner(id, userId);
        Book entity = findBookById(id);
        bookMapper.update(entity, request);
        if (request.authorId() != null && !entity.getAuthor().getId().equals(request.authorId())) {
            Long oldId = entity.getAuthor().getId();
            entity.setAuthor(authorFinder.findById(request.authorId()));
            log.debug("Book with id: {} changed author with id: {} to id: {}", id, oldId, request.authorId());
        }
        if (request.genreId() != null && !entity.getGenre().getId().equals(request.genreId())) {
            Long oldId = entity.getGenre().getId();
            entity.setGenre(genreFinder.findById(request.genreId()));
            log.debug("Book with id: {} changed genre with id: {} to id: {}", id, oldId, request.genreId());
        }
        log.info("Book with id: {} successfully update", id);
        return bookMapper.toResponse(entity);
    }

    private Book findBookById(Long id) {
        log.debug("Find book with id {}", id);
        Book entity = bookRepository.findById(id).orElseThrow(
                () -> new NotFoundException("book.not.found", id)
        );
        log.debug("Book with id {} was found", id);
        return entity;
    }

    private void ensureCorrectOwner(Long bookId, UUID userId) {
        if (!bookRepository.existsBookByIdAndUserId(bookId, userId)) {
            throw new ForbiddenException("book.wrong.owner", bookId, userId);
        }
    }
}
