# Backend — Разработка и отладка

Запуск в режиме разработки (hot-reload не настроен, стандартный процесс):

1. В каталоге `backend` выполнить:

   ./mvnw spring-boot:run

Локальные тесты:

   ./mvnw test

Структура кода:

- Контроллеры: `src/main/java/.../controller`
- Сервисы: `src/main/java/.../service`
- Репозитории: `src/main/java/.../repository`
- Сущности: `src/main/java/.../entity`

Логи и отладка:

- Файлы логов выводятся в консоль. Используется конфигурация логгера в `application.yml`.
