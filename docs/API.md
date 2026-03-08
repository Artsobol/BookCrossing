# API

Базовый URL: `http://localhost:8080`

## Auth

### POST `/api/auth/register`

Регистрация пользователя.

Request:
```json
{
  "username": "john",
  "email": "john@example.com",
  "password": "secret123",
  "confirmPassword": "secret123"
}
```

Response `201 Created`:
```json
{
  "accessToken": "<jwt>",
  "refreshToken": "<raw-refresh-token>",
  "user": {
    "userId": "uuid",
    "username": "john",
    "roles": [
      { "id": 1, "name": "USER" }
    ]
  }
}
```

Также возвращается `Set-Cookie` с refresh token (`refresh_token`).

### POST `/api/auth/login`

Вход пользователя.

Request:
```json
{
  "username": "john",
  "password": "secret123"
}
```

Response `201 Created`: формат как у `/api/auth/register`.

### POST `/api/auth/refresh`

Обновление access token по cookie `refresh_token`.

Request:
- cookie `refresh_token` обязателен
- body не требуется

Response `201 Created`: формат как у `/api/auth/register`, refresh token ротируется.


## Profiles API

Все endpoints профилей требуют `Authorization: Bearer <access-token>`.

- `GET /api/profiles/me` - получить профиль текущего пользователя
- `GET /api/profiles/{username}` - получить профиль по username
- `POST /api/profiles` - создать профиль
- `PATCH /api/profiles` - частично обновить профиль

Пример `POST /api/profiles`:

```json
{
  "firstName": "John",
  "lastName": "Doe",
  "bio": "I like exchanging books"
}
```

Пример ответа:

```json
{
  "id": 1,
  "firstName": "John",
  "lastName": "Doe",
  "bio": "I like exchanging books",
  "user": {
    "id": "uuid",
    "username": "john",
    "roles": [
      { "id": 1, "name": "USER" }
    ]
  }
}
```

## Ошибки

Формат ошибки:
```json
{
  "timestamp": "2026-03-06T10:00:00Z",
  "status": 409,
  "error": "Conflict",
  "errorCode": "CONFLICT",
  "message": "Пользователь с таким именем пользователя уже существует",
  "path": "/api/auth/register"
}
```

Формат ошибки валидации:
```json
{
  "timestamp": "2026-03-06T10:00:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Ошибка валидации",
  "path": "/api/auth/register",
  "errors": [
    { "field": "email", "message": "Некорректный email" }
  ]
}
```

## Логирование и трассировка

- Конфигурация логирования: `src/main/resources/logback-spring.xml`
- Профили: `local`, `dev`, `default`, `prod`
- Корреляция запросов:
    - `X-Request-Id` из входящего запроса или auto-generated UUID
    - поля MDC: `requestId`, `userId`
- Security-ошибки:
    - `401` формируется в `JwtAuthenticationEntryPoint`
    - `403` формируется в `JwtAccessDeniedHandler`
    - ответы сериализуются как JSON (`application/json`, UTF-8)
- Общая стратегия уровней:
    - `WARN` для ожидаемых бизнес/валидационных ошибок
    - `ERROR` для неожиданных исключений с stack trace