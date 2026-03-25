# Changelog

All notable changes to this project are documented in this file.

The format is based on Keep a Changelog and this project follows Semantic Versioning.

## [0.5.1] - 2026-03-25

### Changed

- Refactored entity creation and state changes to use explicit factory and domain methods instead of public setters and
  builders
- Updated service layer to work with entity behavior for refresh token lifecycle and other domain operations
- Adapted mappers to the new entity initialization approach
- Improved domain model consistency by reducing uncontrolled field mutation

## [0.5.0] - 2026-03-10

- Books feature: entity, repository. service, controller, DTOs and mapper
- Books API endpoints: list, get own books, get by id, get all user books, create, update
- Liquibase migration for 'books' table with foreign keys `author_id` to `authors.id`, `genre_id` to `genres.id`, '
  user_id' to `users.id`
- Exception handler for Forbidden exception

## [0.4.0] - 2026-03-10

### Added

- Authors feature: entity, repository, service, controller, DTOs and mapper
- Authors API endpoints: list, get by slug, get by name, create, update
- Liquibase migration for `authors` table with foreign keys `created_by` and `updated_by` to `users.id`
- Exception handler for parameter validations

## [0.3.0] - 2026-03-09

### Added
- Genres feature: entity, repository, service, controller, DTOs and mapper
- Genres API endpoints: list, get by slug, create, update
- Liquibase migration for `genres` table with foreign keys `created_by` and `updated_by` to `users.id`

### Changed
- Project version updated to `0.3.0`
- `Genre` auditing fields now use `UUID` (`createdBy`, `updatedBy`) aligned with `AuditorAware<UUID>`

### Fixed
- Fixed `ClassCastException` in `SecurityAuditorAware` by safely handling non-`UserPrincipal` principals (for example, anonymous principal)
- Fixed auditing type mismatch when persisting `Genre` (`UUID` auditor value can now be written without casting to `User`)

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
