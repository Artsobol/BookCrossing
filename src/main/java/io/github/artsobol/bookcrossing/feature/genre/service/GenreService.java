package io.github.artsobol.bookcrossing.feature.genre.service;

import io.github.artsobol.bookcrossing.feature.genre.dto.CreateGenreRequest;
import io.github.artsobol.bookcrossing.feature.genre.dto.GenreResponse;
import io.github.artsobol.bookcrossing.feature.genre.dto.UpdateGenreRequest;

import java.util.List;

public interface GenreService {

    List<GenreResponse> findAll();

    GenreResponse findBySlug(String slug);

    GenreResponse create(CreateGenreRequest request);

    GenreResponse update(String slug, UpdateGenreRequest request);
}
