package io.github.artsobol.bookcrossing.feature.auth.web;

import io.github.artsobol.bookcrossing.config.properties.security.CookieProperties;
import io.github.artsobol.bookcrossing.feature.auth.dto.request.LoginRequest;
import io.github.artsobol.bookcrossing.feature.auth.dto.request.SessionMetadata;
import io.github.artsobol.bookcrossing.feature.auth.dto.response.AuthResponse;
import io.github.artsobol.bookcrossing.feature.auth.service.LoginService;
import io.github.artsobol.bookcrossing.feature.auth.support.DeviceInfo;
import io.github.artsobol.bookcrossing.feature.auth.support.UserAgentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/auth/login")
@RequiredArgsConstructor
public class LoginController {

    private final CookieProperties properties;
    private final LoginService service;
    private final UserAgentService userAgentService;

    @PostMapping
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest loginRequest,
            HttpServletRequest servletRequest
    ) {
        String userAgent = servletRequest.getHeader(HttpHeaders.USER_AGENT);
        String ipAddress = servletRequest.getRemoteAddr();
        DeviceInfo deviceInfo = getDeviceInfo(userAgent);
        log.info(
                "Received login request for user: {} from IP: {} and device: {}",
                loginRequest.username(),
                ipAddress,
                deviceInfo.device()
        );

        AuthResponse authResponse = service.login(
                loginRequest,
                getSessionMetadata(ipAddress, userAgent, deviceInfo)
        );

        log.info("Login finished for user: {}", loginRequest.username());
        return getResponse(getResponseCookie(authResponse), authResponse);
    }

    private DeviceInfo getDeviceInfo(String userAgent) {
        return userAgentService.parse(userAgent);
    }

    private @NonNull ResponseCookie getResponseCookie(AuthResponse response) {
        return ResponseCookie.from(properties.cookieName(), response.refreshToken())
                .httpOnly(true)
                .secure(properties.secure())
                .sameSite(properties.sameSite())
                .path(properties.path())
                .maxAge(properties.maxAge())
                .build();
    }

    private static SessionMetadata getSessionMetadata(String ipAdress, String userAgent, DeviceInfo deviceInfo) {
        return new SessionMetadata(ipAdress, userAgent, deviceInfo.device());
    }

    private static ResponseEntity<AuthResponse> getResponse(ResponseCookie responseCookie, AuthResponse authResponse) {
        return ResponseEntity.status(201).header(HttpHeaders.SET_COOKIE, responseCookie.toString()).body(authResponse);
    }
}
