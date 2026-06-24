# Диаграмма классов (уровень проектирования)

## Диаграмма

**PlantUML:** [`images/04/class_diagram.puml`](../images/04/class_diagram.puml)

## Ключевые классы backend

| Класс | Пакет | Роль |
|-------|-------|------|
| `AuthController` | controller | `/auth/login`, `/auth/signup` |
| `GroupController` | controller | CRUD группы |
| `TrainingSubjectController` | controller | Multipart предметы |
| `EducationalMaterialController` | controller | Multipart материалы |
| `MaterialCompleteController` | controller | Статусы |
| `AuthService` | Security | JWT, signup, OAuth user |
| `GlobalExceptionHandler` | exception | HTTP-маппинг ошибок |

## Клиентские классы

| Класс | Модуль | Роль |
|-------|--------|------|
| `App` | desktop | JavaFX UI, навигация |
| `BackendApi` | desktop | HTTP-клиент |
| `App.jsx` | frontend | Маршрутизация React |
| `api/index.js` | frontend | Axios + interceptors |
