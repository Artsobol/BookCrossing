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
