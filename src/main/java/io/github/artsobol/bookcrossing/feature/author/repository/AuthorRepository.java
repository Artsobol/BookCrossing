package io.github.artsobol.bookcrossing.feature.author.repository;

import io.github.artsobol.bookcrossing.feature.author.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    Optional<Author> findBySlug(String slug);

    Optional<Author> findByName(String name);

    boolean existsBySlug(String slug);
}
