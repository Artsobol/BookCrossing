package io.github.artsobol.bookcrossing.feature.auth.service;

import io.github.artsobol.bookcrossing.feature.auth.dto.response.AuthResponse;
import io.github.artsobol.bookcrossing.feature.auth.dto.response.UserInfo;
import io.github.artsobol.bookcrossing.feature.refreshtoken.dto.request.CreateRefreshTokenRequest;
import io.github.artsobol.bookcrossing.feature.refreshtoken.service.RefreshTokenService;
import io.github.artsobol.bookcrossing.feature.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthResponseFactory {

    private final AccessTokenService accessTokenService;
    private final RefreshTokenService refreshTokenService;

    public AuthResponse create(CreateRefreshTokenRequest request) {
        log.info("Creating auth response for user: {}", request.user().getUsername());
        User user = request.user();
        AuthResponse response = new AuthResponse(
                accessTokenService.createAccessToken(user),
                refreshTokenService.createRefreshToken(request),
                new UserInfo(user.getId(), user.getUsername(), user.getRoles())
        );

        log.info("Auth response created for user: {}", user.getUsername());
        return response;
    }

    public AuthResponse createWithRefresh(User user, String rawRefreshToken) {
        log.info("Creating auth response with done refresh token for user: {}", user.getUsername());
        AuthResponse response = new AuthResponse(
                accessTokenService.createAccessToken(user),
                rawRefreshToken,
                new UserInfo(user.getId(), user.getEmail(), user.getRoles())
        );

        log.info("Auth response with done refresh token created for user: {}", user.getUsername());
        return response;
    }
}
