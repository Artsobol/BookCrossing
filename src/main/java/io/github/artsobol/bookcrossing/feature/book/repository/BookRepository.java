package io.github.artsobol.bookcrossing.feature.book.repository;

import io.github.artsobol.bookcrossing.feature.book.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findAllByUserId(UUID userId);

    boolean existsBookByIdAndUserId(Long id, UUID userId);
}
