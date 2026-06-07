# Отчёт статического анализа

## Инструменты

| Инструмент | Область | Назначение |
|------------|---------|------------|
| Maven Compiler | backend | Проверка типов Java 17 |
| ESLint (при настройке) | frontend | Стиль JS/JSX |
| Vitest + coverage | frontend | v8 coverage, `npm run coverage` |
| JUnit 5 | backend, desktop | Unit-тесты |

## Результаты (сводка)

### Backend

- Компиляция без ошибок при `./mvnw test`
- Основные предупреждения: неиспользуемые импорты в отдельных классах; legacy `Role` entity
- Security: CSRF отключён (типично для stateless JWT API)

### Frontend

- Покрытие по отчёту `frontend/coverage/`: **~91%** statements/lines на протестированных UI-компонентах
- Страницы (`pages/`) в основном без unit-тестов — зона роста

### Desktop

- Минимальные тесты (`BackendApiReflectionTest`, model tests)
- Крупный класс `App` — повышенная сложность (рекомендация: декомпозиция)

## Типовые замечания

| Категория | Пример | Риск | Действие |
|-----------|--------|------|----------|
| Dead code | `entity/Role` без repository | Низкий | Удалить или реализовать |
| Duplication | Дублирующий route `/groups` в `App.jsx` | Низкий | Оставить один Route |
| Security | JWT в localStorage | Средний | HTTPS обязателен в prod |
| Files | Upload без антивируса | Средний | Ограничить MIME/размер в `FileService` |

## Вывод

Статический анализ и unit-тесты подтверждают работоспособность ядра (auth, material complete, UI components). Для production рекомендуется расширить интеграционные тесты и валидацию загружаемых файлов.
