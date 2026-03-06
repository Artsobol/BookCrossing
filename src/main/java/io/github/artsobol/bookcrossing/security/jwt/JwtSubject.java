package io.github.artsobol.bookcrossing.security.jwt;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.UUID;

public record JwtSubject(
        UUID userId,
        Collection<? extends GrantedAuthority> authorities,
        String username
) {
}
