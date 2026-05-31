# Frontend — Разработка и отладка

Запуск в режиме разработки:

1. Перейти в папку `frontend`:

   cd frontend

2. Запустить dev-сервер:

   npm run dev

Открыть в браузере адрес, указанный в консоли (обычно `http://localhost:5173`).

Советы по разработке:

- Компоненты находятся в `src/components/`.
- Контекст аутентификации: `src/context/AuthContext.jsx`.
- API-обёртка: `src/api/index.js`.

Тесты:

Запуск unit-тестов с Vitest:

   npm run test
