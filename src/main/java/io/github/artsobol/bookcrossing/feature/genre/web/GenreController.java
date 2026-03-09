package io.github.artsobol.bookcrossing.feature.genre.web;

import io.github.artsobol.bookcrossing.feature.genre.dto.CreateGenreRequest;
import io.github.artsobol.bookcrossing.feature.genre.dto.GenreResponse;
import io.github.artsobol.bookcrossing.feature.genre.dto.UpdateGenreRequest;
import io.github.artsobol.bookcrossing.feature.genre.service.GenreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/genres")
@RequiredArgsConstructor
public class GenreController {

    private final GenreService service;

    @GetMapping
    public ResponseEntity<List<GenreResponse>> findAll() {
        log.debug("Request received to get all genres");
        List<GenreResponse> genres = service.findAll();
        log.debug("Return {} genres", genres.size());
        return ResponseEntity.ok(genres);
    }

    @GetMapping("/{slug}")
    public ResponseEntity<GenreResponse> findBySlug(@PathVariable String slug) {
        log.debug("Request received to get genre by slug: {}", slug);
        GenreResponse response = service.findBySlug(slug);
        log.debug("Return genre with slug: {}", response.slug());
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<GenreResponse> create(@RequestBody @Valid CreateGenreRequest request) {
        log.info("Request received to create genre with slug: {}", request.slug());
        GenreResponse response = service.create(request);
        log.info("Genre created with slug: {}", response.slug());
        return ResponseEntity.status(201).body(response);
    }

    @PatchMapping("/{slug}")
    public ResponseEntity<GenreResponse> update(
            @PathVariable String slug,
            @RequestBody @Valid UpdateGenreRequest request
    ) {
        log.info("Request received to update genre with slug: {}", slug);
        GenreResponse response = service.update(slug, request);
        log.info("Genre updated with slug: {}", response.slug());
        return ResponseEntity.ok(response);
    }
}
