package io.github.artsobol.bookcrossing.feature.auth.service;

import io.github.artsobol.bookcrossing.feature.auth.dto.response.AuthResponse;
import io.github.artsobol.bookcrossing.feature.refreshtoken.dto.request.RotateRefreshTokenRequest;

public interface RefreshService {

    AuthResponse refresh(RotateRefreshTokenRequest request);
}
