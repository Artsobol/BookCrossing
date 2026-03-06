package io.github.artsobol.bookcrossing.infrastructure.web.useragent;

public record DeviceInfo(
        String browser,
        String browserVersion,
        String device
) {}