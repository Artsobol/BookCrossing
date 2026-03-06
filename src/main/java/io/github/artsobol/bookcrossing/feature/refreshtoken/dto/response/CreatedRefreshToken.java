package io.github.artsobol.bookcrossing.feature.refreshtoken.dto.response;

import io.github.artsobol.bookcrossing.feature.refreshtoken.entity.RefreshToken;

public record CreatedRefreshToken(
        String rawToken,
        RefreshToken refreshToken
) {
}
