package io.github.artsobol.bookcrossing.feature.author.service;

import io.github.artsobol.bookcrossing.feature.author.dto.AuthorResponse;
import io.github.artsobol.bookcrossing.feature.author.dto.CreateAuthorRequest;
import io.github.artsobol.bookcrossing.feature.author.dto.UpdateAuthorRequest;

import java.util.List;

public interface AuthorService {

    List<AuthorResponse> findAll();

    AuthorResponse create(CreateAuthorRequest request);

    AuthorResponse findBySlug(String slug);

    AuthorResponse findByName(String name);

    AuthorResponse update(String slug, UpdateAuthorRequest request);
}
