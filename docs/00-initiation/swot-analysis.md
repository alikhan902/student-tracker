# SWOT-анализ — Student Tracker

## Strengths (сильные стороны)

- Полный стек Enterprise: web + desktop + API + БД
- Единый REST API для обоих клиентов
- JWT + OAuth2 (Google, GitHub)
- Docker Compose для воспроизводимого запуска
- Документация API через springdoc OpenAPI

## Weaknesses (слабые стороны)

- Десктоп: монолитный `App.java` (~950 строк)
- Интеграционные тесты backend ограничены
- Роли `Role` / `Auditor` частично не используются в коде
- Конфигурация `application.yml` может поставляться отдельно от репозитория

## Opportunities (возможности)

- OpenAPI-генерация клиентов для desktop
- Push-уведомления о новых материалах
- Экспорт отчёта выполнения по группе
- Refresh-токены для длительных сессий

## Threats (угрозы)

- Утечка JWT при XSS (mitigation: HTTPS, короткий TTL секрета)
- Переполнение диска каталогом `uploads/`
- Зависимость от внешних OAuth-провайдеров
- Сжатые сроки курсового проекта

## Стратегии

| Квадрант | Действие |
|----------|----------|
| S+O | Использовать Swagger для автогенерации SDK desktop |
| W+O | Постепенно выделить desktop-контроллеры из `App.java` |
| S+T | HTTPS через Nginx, BCrypt для паролей |
| W+T | Приоритет unit-тестов сервисов и FileService |
