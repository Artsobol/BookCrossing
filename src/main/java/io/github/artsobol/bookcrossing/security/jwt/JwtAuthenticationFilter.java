package io.github.artsobol.bookcrossing.security.jwt;

import io.github.artsobol.bookcrossing.exception.http.BadRequestException;
import io.github.artsobol.bookcrossing.security.user.UserPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        String header = request.getHeader("Authorization");
        if (!checkHeader(header)) {
            filterChain.doFilter(request, response);
            return;
        }

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            Claims claims = parseToken(header);
            UserPrincipal userPrincipal = createUserPrincipal(claims);
            createAuthentication(userPrincipal, getAuthorities(claims));
            filterChain.doFilter(request, response);
        }
    }

    private  void createAuthentication(UserPrincipal userPrincipal, List<SimpleGrantedAuthority> authorities) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(userPrincipal, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private UserPrincipal createUserPrincipal(Claims claims) {
        UUID userId = UUID.fromString(claims.getSubject());
        String username = claims.get("username", String.class);

        return new UserPrincipal(userId, username, getAuthorities(claims));
    }

    private List<SimpleGrantedAuthority> getAuthorities(Claims claims) {
        List<String> roles = claims.get("roles", List.class);
        ensureRoleExists(roles);
        return roles.stream().map(SimpleGrantedAuthority::new).toList();
    }

    private void ensureRoleExists(List<String> roles) {
        if (roles == null || roles.isEmpty()) {
            throw new BadRequestException("token.role.missing");
        }
    }


    private Claims parseToken(String header) {
        try {
            String token = header.substring(7);
            return jwtTokenProvider.parseToken(token);
        } catch (JwtException e) {
            throw new BadRequestException("token.invalid");
        }
    }


    private boolean checkHeader(String header) {
        return header != null && header.startsWith("Bearer ");
    }
}
