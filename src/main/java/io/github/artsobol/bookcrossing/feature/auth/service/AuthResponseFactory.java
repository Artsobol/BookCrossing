package io.github.artsobol.bookcrossing.feature.auth.service;

import io.github.artsobol.bookcrossing.feature.auth.dto.response.AuthResponse;
import io.github.artsobol.bookcrossing.feature.auth.dto.response.UserInfo;
import io.github.artsobol.bookcrossing.feature.refreshtoken.dto.request.CreateRefreshTokenRequest;
import io.github.artsobol.bookcrossing.feature.refreshtoken.service.RefreshTokenService;
import io.github.artsobol.bookcrossing.feature.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthResponseFactory {

    private final AccessTokenService accessTokenService;
    private final RefreshTokenService refreshTokenService;

    public AuthResponse create(CreateRefreshTokenRequest request) {
        User user = request.user();
        return new AuthResponse(
                accessTokenService.createAccessToken(user),
                refreshTokenService.createRefreshToken(request),
                new UserInfo(user.getId(), user.getUsername(), user.getRoles())
        );
    }

    public AuthResponse createWithRefresh(User user, String rawRefreshToken) {
        return new AuthResponse(
                accessTokenService.createAccessToken(user),
                rawRefreshToken,
                new UserInfo(user.getId(), user.getEmail(), user.getRoles())
        );
    }
}
