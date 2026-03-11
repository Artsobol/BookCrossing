package io.github.artsobol.bookcrossing.feature.author.service;

import io.github.artsobol.bookcrossing.feature.author.entity.Author;

public interface AuthorFinder {

    Author findById(Long id);
}
