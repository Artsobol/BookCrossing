package io.github.artsobol.bookcrossing.feature.genre.service;

import io.github.artsobol.bookcrossing.exception.http.ConflictException;
import io.github.artsobol.bookcrossing.exception.http.NotFoundException;
import io.github.artsobol.bookcrossing.feature.genre.dto.CreateGenreRequest;
import io.github.artsobol.bookcrossing.feature.genre.dto.GenreResponse;
import io.github.artsobol.bookcrossing.feature.genre.dto.UpdateGenreRequest;
import io.github.artsobol.bookcrossing.feature.genre.entity.Genre;
import io.github.artsobol.bookcrossing.feature.genre.mapper.GenreMapper;
import io.github.artsobol.bookcrossing.feature.genre.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService, GenreFinder {

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

        Genre entity = Genre.create(request.title(), request.description(), request.slug());
        Genre saved = genreRepository.save(entity);
        log.info("Genre created with id: {} and slug: {}", saved.getId(), saved.getSlug());
        return genreMapper.toResponse(saved);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public GenreResponse update(String slug, UpdateGenreRequest request) {
        log.info("Updating genre with slug: {}", slug);
        Genre entity = getGenreBySlug(slug);
        String currentSlug = request.slug();

        updateGenreSlug(request, currentSlug, entity);
        updateGenreDetails(entity, request.title(), request.description());
        Genre saved = genreRepository.save(entity);

        log.info("Genre updated with id: {} and slug: {}", saved.getId(), saved.getSlug());
        return genreMapper.toResponse(saved);
    }

    private void updateGenreSlug(UpdateGenreRequest request, String currentSlug, Genre entity) {
        if (currentSlug != null && !currentSlug.equals(entity.getSlug())) {
            ensureGenreNotExists(request.slug());
            entity.updateSlug(request.slug());
            log.debug("Genre with id: {} changed slug from: {} to: {}", entity.getId(), currentSlug, entity.getSlug());
        }
    }

    private void updateGenreDetails(Genre entity, String title, String description) {
        if (title != null) {
            entity.updateTitle(title);
        }
        if (description != null) {
            entity.updateDescription(description);
        }
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

    @Override
    public Genre findById(Long id) {
        log.debug("Get genre by id: {}", id);
        return genreRepository.findById(id).orElseThrow(
                () -> new NotFoundException("genre.id.not.found", id)
        );
    }
}
