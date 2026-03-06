package io.github.artsobol.bookcrossing.feature.auth.service;

import io.github.artsobol.bookcrossing.exception.security.AuthenticationException;
import io.github.artsobol.bookcrossing.feature.auth.dto.request.LoginRequest;
import io.github.artsobol.bookcrossing.feature.auth.dto.request.SessionMetadata;
import io.github.artsobol.bookcrossing.feature.auth.dto.response.AuthResponse;
import io.github.artsobol.bookcrossing.feature.refreshtoken.dto.request.CreateRefreshTokenRequest;
import io.github.artsobol.bookcrossing.feature.user.entity.User;
import io.github.artsobol.bookcrossing.feature.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final UserService userService;
    private final AuthResponseFactory authResponseFactory;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthResponse login(LoginRequest request, SessionMetadata meta) {
        User user = userService.findByUsername(request.username());
        ensureCredentialsValid(request.password(), user.getPasswordHash());

        UUID sessionId = UUID.randomUUID();

        CreateRefreshTokenRequest refreshTokenRequest = new CreateRefreshTokenRequest(
            user, sessionId, meta.ipAddress(), meta.userAgent(), meta.deviceName()
        );
        return authResponseFactory.create(refreshTokenRequest);
    }

    private void ensureCredentialsValid(String password, String confirmPassword) {
        if (!passwordEncoder.matches(password, confirmPassword)) {
            throw new AuthenticationException("auth.bad-credentials");
        }
    }
}
