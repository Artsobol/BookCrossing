package io.github.artsobol.bookcrossing.feature.user.service;

import io.github.artsobol.bookcrossing.feature.user.dto.request.CreateUserRequest;
import io.github.artsobol.bookcrossing.feature.user.entity.User;

public interface UserService {

    User createUser(CreateUserRequest request);

    User findByUsername(String username);
}
