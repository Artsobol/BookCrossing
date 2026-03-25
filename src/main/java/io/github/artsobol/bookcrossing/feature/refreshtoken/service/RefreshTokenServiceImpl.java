package io.github.artsobol.bookcrossing.feature.refreshtoken.service;

import io.github.artsobol.bookcrossing.config.properties.security.SessionProperties;
import io.github.artsobol.bookcrossing.exception.security.AuthenticationException;
import io.github.artsobol.bookcrossing.feature.refreshtoken.dto.request.CreateRefreshTokenRequest;
import io.github.artsobol.bookcrossing.feature.refreshtoken.dto.request.RotateRefreshTokenRequest;
import io.github.artsobol.bookcrossing.feature.refreshtoken.dto.response.CreatedRefreshToken;
import io.github.artsobol.bookcrossing.feature.refreshtoken.dto.response.RefreshTokenRotationResult;
import io.github.artsobol.bookcrossing.feature.refreshtoken.entity.RefreshToken;
import io.github.artsobol.bookcrossing.feature.refreshtoken.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenEncoder encoder;
    private final SessionProperties properties;

    @Override
    @Transactional
    public String createRefreshToken(CreateRefreshTokenRequest request) {
        log.info("Start creating refresh token for user: {}", request.user().getUsername());
        UUID id = request.user().getId();
        long activeSessions = refreshTokenRepository.countActiveSessions(id);
        ensureHasSessions(id, activeSessions);
        CreatedRefreshToken encoded = encoder.create(request);
        refreshTokenRepository.save(encoded.refreshToken());

        log.info("Refresh token created for user: {}", request.user().getUsername());
        return encoded.rawToken();
    }

    @Override
    @Transactional
    public RefreshTokenRotationResult rotate(RotateRefreshTokenRequest request) {
        log.info("Start rotating refresh token");
        String hash = encoder.hash(request.rawRefreshToken());
        RefreshToken token = refreshTokenRepository.findByTokenHash(hash)
                .orElseThrow(() -> new AuthenticationException("auth.refresh.invalid"));

        if (token.isExpiredAt(Instant.now())) {
            throw new AuthenticationException("auth.refresh.expired");
        }

        if (token.isRevoked()) {
            throw new AuthenticationException("auth.refresh.revoked");
        }

        CreateRefreshTokenRequest refreshTokenRequest = new CreateRefreshTokenRequest(
                token.getUser(),
                token.getSessionId(),
                request.ipAddress(),
                request.userAgent(),
                token.getDeviceName()
        );
        CreatedRefreshToken encoded = encoder.create(refreshTokenRequest);
        RefreshToken newToken = encoded.refreshToken();

        token.replaceWith(newToken, Instant.now());

        refreshTokenRepository.save(newToken);

        log.info("Refresh token rotated");
        return new RefreshTokenRotationResult(token.getUser(), encoded.rawToken());
    }

    private void ensureHasSessions(UUID userId, long sessionsCount) {
        if (sessionsCount >= properties.maxSessions()) {
            log.info("User: {} has too many active sessions={}, revoking oldest one", userId, sessionsCount);
            RefreshToken token = refreshTokenRepository.findOldestActiveSessions(userId, PageRequest.of(0, 1))
                    .getFirst();
            refreshTokenRepository.revokeSessionByUserIdAndSessionId(userId, token.getSessionId());
        }
    }
}
