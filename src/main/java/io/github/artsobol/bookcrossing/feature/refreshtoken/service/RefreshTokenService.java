package io.github.artsobol.bookcrossing.feature.refreshtoken.service;

import io.github.artsobol.bookcrossing.feature.refreshtoken.dto.request.CreateRefreshTokenRequest;
import io.github.artsobol.bookcrossing.feature.refreshtoken.dto.request.RotateRefreshTokenRequest;
import io.github.artsobol.bookcrossing.feature.refreshtoken.dto.response.RefreshTokenRotationResult;

public interface RefreshTokenService {

    String createRefreshToken(CreateRefreshTokenRequest request);

    RefreshTokenRotationResult rotate(RotateRefreshTokenRequest request);
}
