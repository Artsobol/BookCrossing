package io.github.artsobol.bookcrossing.feature.refreshtoken.service;

import io.github.artsobol.bookcrossing.exception.security.AuthenticationException;
import io.github.artsobol.bookcrossing.feature.refreshtoken.dto.request.CreateRefreshTokenRequest;
import io.github.artsobol.bookcrossing.feature.refreshtoken.dto.request.RotateRefreshTokenRequest;
import io.github.artsobol.bookcrossing.feature.refreshtoken.dto.response.CreatedRefreshToken;
import io.github.artsobol.bookcrossing.feature.refreshtoken.dto.response.RefreshTokenRotationResult;
import io.github.artsobol.bookcrossing.feature.refreshtoken.entity.RefreshToken;
import io.github.artsobol.bookcrossing.config.SessionProperties;
import io.github.artsobol.bookcrossing.feature.refreshtoken.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenEncoder encoder;
    private final SessionProperties properties;

    @Override
    @Transactional
    public String createRefreshToken(CreateRefreshTokenRequest request) {
        UUID id = request.user().getId();
        long activeSessions = refreshTokenRepository.countActiveSessions(id);
        ensureHasSessions(id, activeSessions);
        CreatedRefreshToken encoded = encoder.create(request);
        refreshTokenRepository.save(encoded.refreshToken());
        return encoded.rawToken();
    }

    @Override
    @Transactional
    public RefreshTokenRotationResult rotate(RotateRefreshTokenRequest request) {
        String hash = encoder.hash(request.rawRefreshToken());
        RefreshToken token = refreshTokenRepository.findByTokenHash(hash)
                .orElseThrow(() -> new AuthenticationException("auth.refresh.invalid"));

        if (token.getExpiresAt().isBefore(Instant.now())) {
            throw new AuthenticationException("auth.refresh.expired");
        }

        if (token.getRevokedAt() != null) {
            throw new AuthenticationException("auth.refresh.revoked");
        }

        Instant now = Instant.now();
        token.setRevokedAt(now);

        CreateRefreshTokenRequest refreshTokenRequest = new CreateRefreshTokenRequest(
                token.getUser(),
                token.getSessionId(),
                request.ipAddress(),
                request.userAgent(),
                token.getDeviceName()
        );
        CreatedRefreshToken encoded = encoder.create(refreshTokenRequest);
        RefreshToken newToken = encoded.refreshToken();

        refreshTokenRepository.save(newToken);

        token.setReplaceBy(newToken);
        newToken.setReplacedToken(token);

        return new RefreshTokenRotationResult(token.getUser(), encoded.rawToken());
    }

    private void ensureHasSessions(UUID userId, long sessionsCount) {
        if (sessionsCount >= properties.maxSessions()) {
            RefreshToken token = refreshTokenRepository.findOldestActiveSessions(userId, PageRequest.of(0, 1))
                    .getFirst();
            refreshTokenRepository.revokeSessionByUserIdAndSessionId(userId, token.getSessionId());
        }
    }
}
