package io.github.artsobol.bookcrossing.feature.auth.service;

import io.github.artsobol.bookcrossing.feature.user.entity.User;
import io.github.artsobol.bookcrossing.security.jwt.JwtSubject;
import io.github.artsobol.bookcrossing.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccessTokenServiceImpl implements AccessTokenService {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public String createAccessToken(User user) {
        log.info("Creating access token for user: {}", user.getUsername());
        Set<GrantedAuthority> authorities = user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(
                Collectors.toSet());
        JwtSubject subject = new JwtSubject(user.getId(), authorities, user.getUsername());
        String token = jwtTokenProvider.generateToken(subject);

        log.info("Access token created for user: {}", user.getUsername());
        return token;
    }
}
