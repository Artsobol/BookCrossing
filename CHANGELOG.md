# Changelog

All notable changes to this project are documented in this file.

The format is based on Keep a Changelog and this project follows Semantic Versioning.

## [0.2.1] - 2026-03-08

### Added
- Centralized logging configuration in `logback-spring.xml` for `local`, `dev`, `default`, and `prod` profiles
- Request correlation via `MdcFilter` with `X-Request-Id` -> `MDC requestId`
- `userId` enrichment in MDC after successful JWT authentication
- Structured security logs for unauthorized (`401`) and access denied (`403`) scenarios

### Changed
- Security error responses for `AuthenticationEntryPoint` and `AccessDeniedHandler` are now written as JSON with UTF-8 encoding
- `JwtAuthenticationFilter` behavior updated for invalid JWT: no business exception leakage from filter layer, security context is cleared, request continues through filter chain
- Logging levels and messages in security filter/advice refined to reduce noise and avoid sensitive header value output
- `CommonControllerAdvice` logging strategy aligned to severity (`WARN` for expected business/validation failures, `ERROR` for unexpected failures with exception stack trace)

### Fixed
- Fixed malformed/unsafe security filter flow where request processing could stop without continuing chain in specific branches
- Fixed response handling guard in security handlers via `response.isCommitted()` checks
- Fixed `logback-spring.xml` file appender pattern and default `LOG_PATH` fallback usage

## [0.2.0] - 2026-03-07

### Added
- User profiles feature (`profile` entity, repository, service, controller)
- DTOs for profile create/update/response
- MapStruct mapper for profile mapping
- Liquibase migration for profile schema
- MapStruct integration for DTO/entity mapping

### Changed
- Project version updated from `0.1.0` to `0.2.0`
- User-related APIs and response models extended to support profile data

## [0.1.0] - 2026-03-06

### Added
- JWT authentication flow
- Refresh token infrastructure and rotation DTOs
- Login, registration, and refresh endpoints/services
- User module (entity, repository, service, DTOs)
- Role module (entity, repository, service, DTOs)
- Liquibase migrations and base DB schema
- JPA auditing configuration
- Localization (i18n) for API messages
- Custom exception hierarchy and HTTP error handling
- Project documentation (`README.md`)

### Changed
- Project refactoring and package structure cleanup
- Build and configuration updates for security and application properties
