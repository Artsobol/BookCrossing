package io.github.artsobol.bookcrossing.feature.auth.service;

import io.github.artsobol.bookcrossing.feature.user.entity.User;

public interface AccessTokenService {

    String createAccessToken(User user);
}
