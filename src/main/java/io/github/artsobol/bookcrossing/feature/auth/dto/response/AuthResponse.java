package io.github.artsobol.bookcrossing.feature.auth.dto.response;

public record AuthResponse(
        String accessToken, String refreshToken, UserInfo user
) {
}
