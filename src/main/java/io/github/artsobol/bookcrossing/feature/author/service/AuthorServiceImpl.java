package io.github.artsobol.bookcrossing.feature.author.service;

import io.github.artsobol.bookcrossing.exception.http.ConflictException;
import io.github.artsobol.bookcrossing.exception.http.NotFoundException;
import io.github.artsobol.bookcrossing.feature.author.dto.AuthorResponse;
import io.github.artsobol.bookcrossing.feature.author.dto.CreateAuthorRequest;
import io.github.artsobol.bookcrossing.feature.author.dto.UpdateAuthorRequest;
import io.github.artsobol.bookcrossing.feature.author.entity.Author;
import io.github.artsobol.bookcrossing.feature.author.mapper.AuthorMapper;
import io.github.artsobol.bookcrossing.feature.author.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService, AuthorFinder {

    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    @Override
    public List<AuthorResponse> findAll() {
        log.debug("Finding all authors");
        List<AuthorResponse> response = authorRepository.findAll().stream().map(authorMapper::toResponse).toList();
        log.debug("Found {} authors", response.size());
        return response;
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public AuthorResponse create(CreateAuthorRequest request) {
        log.info("Creating author with slug: {}", request.slug());
        ensureAuthorNotExists(request.slug());
        Author entity = authorMapper.toEntity(request);
        Author saved = authorRepository.save(entity);
        log.info("Author created with id: {} and slug: {}", saved.getId(), saved.getSlug());
        return authorMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public AuthorResponse findBySlug(String slug) {
        log.debug("Finding author by slug: {}", slug);
        Author entity = getAuthorBySlug(slug);
        log.debug("Author found with slug: {}", entity.getSlug());
        return authorMapper.toResponse(entity);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public AuthorResponse findByName(String name) {
        log.debug("Finding author by name: {}", name);
        Author entity = getAuthorByName(name);
        log.debug("Author found with name: {}", entity.getName());
        return authorMapper.toResponse(entity);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public AuthorResponse update(String slug, UpdateAuthorRequest request) {
        log.info("Updating author with slug: {}", slug);
        if (request.slug() != null && !request.slug().equals(slug)) {
            ensureAuthorNotExists(request.slug());
        }
        Author entity = getAuthorBySlug(slug);
        authorMapper.update(entity, request);
        Author saved = authorRepository.save(entity);
        log.info("Author updated with id: {} and slug: {}", saved.getId(), saved.getSlug());
        return authorMapper.toResponse(saved);
    }

    private Author getAuthorByName(String name) {
        return authorRepository.findByName(name).orElseThrow(
                () -> new NotFoundException("author.not.found.name", name)
        );
    }

    private Author getAuthorBySlug(String slug) {
        return authorRepository.findBySlug(slug).orElseThrow(
                () -> new NotFoundException("author.not.found.slug", slug)
        );
    }

    private void ensureAuthorNotExists(String slug) {
        log.debug("Checking if author with slug: {} already exists", slug);
        if (authorRepository.existsBySlug(slug)) {
            throw new ConflictException("author.slug.exists");
        }
    }

    @Override
    public Author findById(Long id) {
        log.debug("Finding author with id: {}", id);
        return authorRepository.findById(id).orElseThrow(
                () -> new NotFoundException("author.not.found.id", id)
        );
    }
}
