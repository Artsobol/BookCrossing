package io.github.artsobol.bookcrossing.feature.auth.service;

import io.github.artsobol.bookcrossing.exception.http.ConflictException;
import io.github.artsobol.bookcrossing.feature.auth.dto.request.RegistrationRequest;
import io.github.artsobol.bookcrossing.feature.auth.dto.request.SessionMetadata;
import io.github.artsobol.bookcrossing.feature.auth.dto.response.AuthResponse;
import io.github.artsobol.bookcrossing.feature.refreshtoken.dto.request.CreateRefreshTokenRequest;
import io.github.artsobol.bookcrossing.feature.user.dto.request.CreateUserRequest;
import io.github.artsobol.bookcrossing.feature.user.entity.User;
import io.github.artsobol.bookcrossing.feature.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {

    private final PasswordEncoder passwordEncoder;
    private final AuthResponseFactory authResponseFactory;
    private final UserService userService;

    @Override
    public AuthResponse register(RegistrationRequest request, SessionMetadata meta) {
        log.info("Registration started for username: {}", request.username());
        ensurePasswordsMatch(request.password(), request.confirmPassword());

        CreateUserRequest userRequest = new CreateUserRequest(
                request.username(),
                request.email(),
                passwordEncoder.encode(request.password())
        );
        User user = userService.createUser(userRequest);

        UUID sessionId = UUID.randomUUID();

        CreateRefreshTokenRequest refreshTokenRequest = new CreateRefreshTokenRequest(
                user,
                sessionId,
                meta.ipAddress(),
                meta.userAgent(),
                meta.deviceName()
        );

        AuthResponse response = authResponseFactory.create(refreshTokenRequest);
        log.info("Registration finished for username: {}", request.username());

        return response;
    }

    private void ensurePasswordsMatch(String password, String passwordConfirmation) {
        log.debug("Checking if passwords match");
        if (!password.equals(passwordConfirmation)) {
            throw new ConflictException("auth.password.mismatch");
        }
    }
}
