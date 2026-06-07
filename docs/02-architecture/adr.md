# Архитектурные решения (ADR)

## ADR-001: Паттерн PCMEF

**Статус:** Принято · **Дата:** 01.01.2026

### Контекст
Требуется разделение ответственности серверной части с возможностью независимого тестирования слоёв.

### Решение
PCMEF, маппинг на Spring Boot:
- **P** → React (`frontend/`) + JavaFX (`desktop/`)
- **C** → `controller/`
- **M** → `service/`, `service/impl/`, `Security/`
- **E** → `entity/`
- **F** → `Repository/`

### Последствия
Контроллеры тонкие; бизнес-правила в сервисах; JPA изолирована от HTTP.

---

## ADR-002: JWT + OAuth2

**Статус:** Принято · **Дата:** 01.01.2026

### Контекст
Web и desktop должны использовать единую stateless-аутентификацию; нужен вход через Google/GitHub.

### Решение
JJWT 0.12.6 + Spring Security OAuth2 Client. После OAuth2 — редирект на фронт с `token` и `userId`.

### Последствия
`SessionCreationPolicy.STATELESS`; заголовок `Authorization: Bearer`; refresh-токены не реализованы.

---

## ADR-003: Docker Compose

**Статус:** Принято · **Дата:** 01.2026

### Решение
Сервисы: `postgres`, `backend`, `frontend` (Vite dev), `nginx` (80/443).

### Последствия
`docker compose up --build`; SSL на Nginx; `APP_FRONTEND_URL` для OAuth redirect.

---

## ADR-004: Два клиента на одном API

**Статус:** Принято

### Решение
React (Axios) и JavaFX (`BackendApi` + Jackson) → один Spring Boot backend.

---

## ADR-005: ModelMapper для Entity ↔ DTO

**Статус:** Принято

### Решение
ModelMapper 3.2.0 в сервисном слое; DTO в пакете `dto/`.

---

## ADR-006: Составной ключ MaterialComplete

**Статус:** Принято

### Контекст
Статус выполнения уникален для пары (пользователь, материал).

### Решение
`@EmbeddedId MaterialCompleteId` + `@MapsId` на связях с `User` и `EducationalMaterial`.
