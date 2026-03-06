package io.github.artsobol.bookcrossing.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.security.session")
public record SessionProperties(
        long maxSessions
) {
}
