package io.github.artsobol.bookcrossing.feature.refreshtoken.dto.response;

import io.github.artsobol.bookcrossing.feature.user.entity.User;

public record RefreshTokenRotationResult(
        User user,
        String rawRefreshToken
) {
}
