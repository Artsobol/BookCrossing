package io.github.artsobol.bookcrossing.feature.author.web;

import io.github.artsobol.bookcrossing.feature.author.dto.AuthorResponse;
import io.github.artsobol.bookcrossing.feature.author.dto.CreateAuthorRequest;
import io.github.artsobol.bookcrossing.feature.author.dto.UpdateAuthorRequest;
import io.github.artsobol.bookcrossing.feature.author.service.AuthorService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/authors")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService service;

    @GetMapping
    public ResponseEntity<List<AuthorResponse>> findAll() {
        log.debug("Request received to get all authors");
        List<AuthorResponse> authors = service.findAll();
        log.debug("Return {} authors", authors.size());
        return ResponseEntity.ok(authors);
    }

    @GetMapping("/{slug}")
    public ResponseEntity<AuthorResponse> findBySlug(@PathVariable String slug) {
        log.debug("Request received to get author by slug: {}", slug);
        AuthorResponse response = service.findBySlug(slug);
        log.debug("Return author with slug: {}", response.slug());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/name")
    public ResponseEntity<AuthorResponse> findByName(@RequestParam String name) {
        log.debug("Request received to get author by name: {}", name);
        AuthorResponse response = service.findByName(name);
        log.debug("Return author with name: {}", response.name());
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<AuthorResponse> create(@RequestBody @Valid CreateAuthorRequest request) {
        log.info("Request received to create author with slug: {}", request.slug());
        AuthorResponse response = service.create(request);
        log.info("Author created with slug: {}", response.slug());
        return ResponseEntity.status(201).body(response);
    }

    @PatchMapping("/{slug}")
    public ResponseEntity<AuthorResponse> update(
            @PathVariable String slug,
            @RequestBody @Valid UpdateAuthorRequest request
    ) {
        log.info("Request received to update author with slug: {}", slug);
        AuthorResponse response = service.update(slug, request);
        log.info("Author updated with slug: {}", response.slug());
        return ResponseEntity.ok(response);
    }
}
