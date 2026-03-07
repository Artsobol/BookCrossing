# Changelog

All notable changes to this project are documented in this file.

The format is based on Keep a Changelog and this project follows Semantic Versioning.

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
