package io.github.artsobol.bookcrossing.feature.auth.web;

import io.github.artsobol.bookcrossing.config.properties.security.CookieProperties;
import io.github.artsobol.bookcrossing.feature.auth.dto.response.AuthResponse;
import io.github.artsobol.bookcrossing.feature.auth.service.RefreshService;
import io.github.artsobol.bookcrossing.feature.refreshtoken.dto.request.RotateRefreshTokenRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/auth/refresh")
@RequiredArgsConstructor
public class RefreshController {

    private final CookieProperties properties;
    private final RefreshService service;

    @PostMapping
    public ResponseEntity<AuthResponse> login(
            @CookieValue(value = "refresh_token", required = false) String refreshToken,
            HttpServletRequest servletRequest
    ) {
        String userAgent = servletRequest.getHeader(HttpHeaders.USER_AGENT);
        String ipAddress = servletRequest.getRemoteAddr();
        log.info("Received refresh request from IP: {} and device: {}", ipAddress, userAgent);

        RotateRefreshTokenRequest request = new RotateRefreshTokenRequest(refreshToken, ipAddress, userAgent);
        AuthResponse authResponse = service.refresh(request);
        log.info("Refresh request successfully finished");

        return getResponse(getResponseCookie(authResponse), authResponse);
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

    private static ResponseEntity<AuthResponse> getResponse(ResponseCookie responseCookie, AuthResponse authResponse) {
        return ResponseEntity.status(201).header(HttpHeaders.SET_COOKIE, responseCookie.toString()).body(authResponse);
    }
}
