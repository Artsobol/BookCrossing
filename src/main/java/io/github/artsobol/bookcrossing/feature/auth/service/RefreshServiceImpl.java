package io.github.artsobol.bookcrossing.feature.auth.service;

import io.github.artsobol.bookcrossing.feature.auth.dto.response.AuthResponse;
import io.github.artsobol.bookcrossing.feature.refreshtoken.dto.request.RotateRefreshTokenRequest;
import io.github.artsobol.bookcrossing.feature.refreshtoken.dto.response.RefreshTokenRotationResult;
import io.github.artsobol.bookcrossing.feature.refreshtoken.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshServiceImpl implements RefreshService {

    private final RefreshTokenService refreshTokenService;
    private final AuthResponseFactory authResponseFactory;;

    @Override
    @Transactional
    public AuthResponse refresh(RotateRefreshTokenRequest request) {
        log.info("Received request to refresh token");
        RefreshTokenRotationResult rotated = refreshTokenService.rotate(request);
        return authResponseFactory.createWithRefresh(rotated.user(), rotated.rawRefreshToken());
    }
}
