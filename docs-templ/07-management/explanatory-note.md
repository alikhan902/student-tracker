# Пояснительная записка

## kanbaNice — Корпоративная система управления проектами (Kanban)

**Курсовой проект по дисциплине «Программная инженерия»**  
**Траектория Г: Enterprise (полный стек)**  
**Дата:** 01.06.2026

---

## 1. Введение

### 1.1. Цель работы

Разработка корпоративной системы управления проектами по методологии Kanban с применением архитектурного паттерна PCMEF, реализующей два типа клиентов (веб и десктоп) на едином серверном API.

### 1.2. Актуальность

Малые и средние команды нуждаются в простом и гибком инструменте управления задачами. Существующие решения (Jira, Trello) требуют постоянного интернет-соединения и оплаты. kanbaNice предоставляет self-hosted альтернативу, развёртываемую в инфраструктуре заказчика.

---

## 2. Анализ предметной области

Методология Kanban предполагает визуализацию рабочего процесса через доски и карточки (задачи). Ключевые понятия: компания, проект, доска, задача, роль участника.

Были проведены:
- Бизнес-анализ (IDEF0, BUC-диаграмма, SWOT)
- Анализ стейкхолдеров
- Составление бизнес-глоссария (20 терминов)

---

## 3. Проектирование

### 3.1. Архитектура

Выбран паттерн **PCMEF** (Presentation–Control–Mediator–Entity–Foundation):

| Слой | Реализация |
|------|-----------|
| P — Presentation | React SPA + JavaFX 21 |
| C — Control | 6 Spring @RestController |
| M — Mediator | 6 Spring @Service |
| E — Entity | 6 JPA @Entity + перечисления |
| F — Foundation | 5 Spring Data JPA Repository |

Ключевые архитектурные решения задокументированы в 5 ADR (см. docs/02-architecture/adr.md).

### 3.2. База данных

PostgreSQL 16, 7 таблиц, 3NF-нормализация.  
Схема генерируется Hibernate, DDL-скрипты задокументированы.

### 3.3. Безопасность

- BCrypt для хеширования паролей
- JWT (JJWT 0.12.6) для stateless-аутентификации
- Spring Security OAuth2 для входа через Google
- HTTPS через Nginx SSL-терминацию

---

## 4. Реализация

### 4.1. Серверная часть (Backend)

**Java 17 + Spring Boot 3** реализует все 27 REST-эндпоинтов, покрывающих CRUD для всех сущностей. Документация через SpringDoc OpenAPI (Swagger UI доступен на `/swagger-ui.html`).

Ключевые компоненты:
- `JwtAuthFilter` — перехват и проверка JWT
- `OAuth2successHandler` — обработка Google-входа
- `GlobalExceptionHandler` — централизованная обработка ошибок
- `EmailServiceImpl` — отправка писем сброса пароля

### 4.2. Веб-клиент (Frontend)

**React 19 + Vite** — SPA с 7 страницами:
1. LoginPage — форма входа / Google OAuth
2. RegPage — регистрация
3. OAuthRedirect — обработка OAuth callback
4. CompanyPage — управление компанией
5. ProjectsSelectionPage — список проектов
6. ProjectPage — Kanban-доска проекта
7. ProfilePage — профиль пользователя

Состояние управляется через Context API (UserContext, CompanyContext, ProjectContext).

### 4.3. Десктоп-клиент (Desktop)

**JavaFX 21** — нативный GUI с 7 экранами:
1. LoginView, SignupView — аутентификация
2. MainLayout — основной макет с боковым меню
3. CompanyView — управление компанией
4. ProjectsView — список проектов
5. ProjectBoardView — Kanban-доска
6. ProfileView — профиль

Взаимодействует с тем же REST API через `ApiClient` (Jackson + HttpClient).

### 4.4. Инфраструктура

Docker Compose оркестрирует 4 контейнера:
- `postgres` — база данных с persistent volume
- `backend` — Spring Boot (8080)
- `frontend` — статическая сборка React
- `nginx` — реверс-прокси (80/443, SSL)

---

## 5. Применённые паттерны

| Паттерн | Слой | Реализация |
|---------|------|-----------|
| Data Mapper | Mediator | ModelMapper (Entity ↔ DTO) |
| Identity Map | Foundation | Hibernate 1st Level Cache |
| Lazy Load | Entity | FetchType.LAZY в OneToMany |
| Repository | Foundation | Spring Data JPA |
| DTO | Control | 15+ DTO-классов |
| Factory/Builder | Entity | Lombok @Builder |

---

## 6. Соответствие требованиям МУ

### Траектория Г (Enterprise)

| Требование | Выполнение |
|-----------|-----------|
| Desktop UI (5+ экранов) | ✅ 7 экранов JavaFX |
| Web UI (5+ страниц) | ✅ 7 страниц React |
| REST API (15+ эндпоинтов) | ✅ 27 эндпоинтов |
| OpenAPI документация | ✅ Swagger UI |
| JWT + Spring Security | ✅ Реализовано |
| Docker + docker-compose | ✅ Реализовано |
| Интеграционные тесты | ❌ Не реализованы |
| CI/CD pipeline | ❌ Не реализован |

### Общие требования

| Требование | Выполнение |
|-----------|-----------|
| Архитектура PCMEF | ✅ Все 5 слоёв |
| README.md | ✅ Полное описание |
| docs/ по этапам | ✅ Все 8 этапов |
| Data Mapper | ✅ ModelMapper |
| Identity Map | ✅ Hibernate Cache |
| Модульные тесты >40% | ❌ Тесты отсутствуют |
| Статический анализ | ⚠️ Только ручной |

---

## 7. Заключение

В результате работы разработана полнофункциональная корпоративная Kanban-система, реализующая все ключевые требования Траектории Г. Система включает 27 REST-эндпоинтов, два типа клиентов (веб + десктоп), контейнеризацию и безопасную аутентификацию.

**Основной нереализованный элемент:** автоматизированные тесты (JUnit + JaCoCo). Покрытие 0% при требовании >40% — критическое отклонение от МУ, требующее устранения перед финальной сдачей.

---

## 8. Список использованных технологий

1. Java 17 — основной язык серверной части
2. Spring Boot 3 — фреймворк для REST API
3. Spring Security + JJWT 0.12.6 — безопасность и JWT
4. Spring Data JPA / Hibernate — ORM
5. PostgreSQL 16 — реляционная СУБД
6. React 19 + Vite 8 — веб-клиент
7. JavaFX 21 — десктопный клиент
8. SpringDoc OpenAPI 2.5 — документация API
9. ModelMapper 3.2 — маппинг объектов
10. Docker + Docker Compose — контейнеризация
11. Nginx — реверс-прокси с SSL
12. Lombok 1.18 — генерация шаблонного кода
13. Apache POI 5.2 — работа с офисными документами
