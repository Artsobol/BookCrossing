package io.github.artsobol.bookcrossing.feature.genre.repository;

import io.github.artsobol.bookcrossing.feature.genre.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GenreRepository extends JpaRepository<Genre, Long> {

    Optional<Genre> findBySlug(String slug);

    boolean existsBySlug(String slug);
}
