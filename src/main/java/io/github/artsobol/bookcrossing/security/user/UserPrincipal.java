package io.github.artsobol.bookcrossing.security.user;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.UUID;

public record UserPrincipal(
        UUID userId,
        String username,
        Collection<? extends GrantedAuthority> authorities
) {
}
