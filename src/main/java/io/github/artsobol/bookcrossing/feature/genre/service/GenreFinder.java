package io.github.artsobol.bookcrossing.feature.genre.service;

import io.github.artsobol.bookcrossing.feature.genre.entity.Genre;

public interface GenreFinder {

    Genre findById(Long id);
}
