package io.github.artsobol.bookcrossing.feature.user.service;

import io.github.artsobol.bookcrossing.feature.user.entity.User;

import java.util.UUID;

public interface UserFinder {

    User findByUsername(String username);

    User findById(UUID id);
}
