package io.github.artsobol.bookcrossing.feature.refreshtoken.service;

import io.github.artsobol.bookcrossing.feature.refreshtoken.dto.request.CreateRefreshTokenRequest;
import io.github.artsobol.bookcrossing.feature.refreshtoken.dto.response.CreatedRefreshToken;
import io.github.artsobol.bookcrossing.feature.refreshtoken.entity.RefreshToken;
import io.github.artsobol.bookcrossing.config.RefreshTokenProperties;
import io.github.artsobol.bookcrossing.utils.TokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class RefreshTokenEncoder {

    private final RefreshTokenProperties properties;

    public CreatedRefreshToken create(CreateRefreshTokenRequest request) {
        RefreshToken token = createToken(request);

        String rawToken = TokenUtils.generateRawToken(properties.length());
        token.setTokenHash(TokenUtils.hmacSha256Base64Url(rawToken, properties.pepper()));

        return new CreatedRefreshToken(rawToken, token);
    }

    private RefreshToken createToken(CreateRefreshTokenRequest request) {
        return RefreshToken.builder()
                .user(request.user())
                .sessionId(request.sessionId())
                .userAgent(request.userAgent())
                .ipAddress(request.ipAddress())
                .deviceName(request.deviceName())
                .expiresAt(Instant.now().plus(properties.ttl()))
                .lastUsedAt(Instant.now())
                .build();
    }

    public String hash(String rawToken) {
        return TokenUtils.hmacSha256Base64Url(rawToken, properties.pepper());
    }
}
