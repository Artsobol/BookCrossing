package io.github.artsobol.bookcrossing.feature.auth.support;

public record DeviceInfo(
        String browser,
        String browserVersion,
        String device
) {}