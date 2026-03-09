package io.github.artsobol.bookcrossing.feature.genre.service;

import io.github.artsobol.bookcrossing.exception.http.ConflictException;
import io.github.artsobol.bookcrossing.exception.http.NotFoundException;
import io.github.artsobol.bookcrossing.feature.genre.dto.CreateGenreRequest;
import io.github.artsobol.bookcrossing.feature.genre.dto.GenreResponse;
import io.github.artsobol.bookcrossing.feature.genre.dto.UpdateGenreRequest;
import io.github.artsobol.bookcrossing.feature.genre.entity.Genre;
import io.github.artsobol.bookcrossing.feature.genre.repository.GenreRepository;
import io.github.artsobol.bookcrossing.feature.genre.mapper.GenreMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;

    @Override
    public List<GenreResponse> findAll() {
        log.debug("Finding all genres");
        return genreRepository.findAll().stream().map(genreMapper::toResponse).toList();
    }

    @Override
    @PreAuthorize("hasAnyAuthority('USER')")
    public GenreResponse findBySlug(String slug) {
        Genre genre = getGenreBySlug(slug);
        log.debug("Genre found with id: {} and slug: {}", genre.getId(), genre.getSlug());
        return genreMapper.toResponse(genre);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public GenreResponse create(CreateGenreRequest request) {
        log.info("Creating genre with slug: {}", request.slug());
        ensureGenreNotExists(request.slug());
        Genre entity = genreMapper.toEntity(request);
        Genre saved = genreRepository.save(entity);
        log.info("Genre created with id: {} and slug: {}", saved.getId(), saved.getSlug());
        return genreMapper.toResponse(saved);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public GenreResponse update(String slug, UpdateGenreRequest request) {
        log.info("Updating genre with slug: {}", slug);
        Genre entity = getGenreBySlug(slug);
        if (request.slug() != null && !request.slug().equals(entity.getSlug())) {
            ensureGenreNotExists(request.slug());
        }
        genreMapper.update(entity, request);
        Genre saved = genreRepository.save(entity);
        log.info("Genre updated with id: {} and slug: {}", saved.getId(), saved.getSlug());
        return genreMapper.toResponse(saved);
    }

    private Genre getGenreBySlug(String slug) {
        log.debug("Get genre by slug: {}", slug);
        return genreRepository.findBySlug(slug).orElseThrow(() -> new NotFoundException("genre.slug.not.found", slug));
    }

    private void ensureGenreNotExists(String slug) {
        log.debug("Check genre by slug {} for creation", slug);
        if (genreRepository.existsBySlug(slug)) {
            throw new ConflictException("genre.slug.exists");
        }
    }
}
