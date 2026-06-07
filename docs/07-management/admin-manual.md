# Руководство администратора / развёртывание

## 1. Требования

| Компонент | Версия |
|-----------|--------|
| Docker, Docker Compose | актуальные |
| Java (локальная сборка) | 17+ |
| Node.js (локальный frontend) | 18+ |
| PostgreSQL | 16 |

## 2. Конфигурация

```bash
cp .env.example .env
# заполнить POSTGRES_*, SPRING_DATASOURCE_*, JWT_SECRET, OAuth, APP_FRONTEND_URL
cp backend/.example backend/.env  # при локальном запуске backend
```

Ключевые переменные — см. корневой `README.md` и [implementation-notes](../05-implementation/implementation-notes.md).

## 3. Docker Compose

```bash
docker compose up --build -d
docker compose logs -f backend
docker compose down   # остановка
```

### Сервисы

| Сервис | Порт | Описание |
|--------|------|----------|
| postgres | 5432 | БД `student_tracker` |
| backend | 8080 | Spring Boot |
| frontend | 5173 | Vite dev server |
| nginx | 80, 443 | Reverse proxy + SSL |

### Тома

- `postgres_data` — данные БД
- `./backend/uploads` — загруженные файлы

## 4. HTTPS (Nginx)

**Локально (mkcert):**

```bash
mkcert -install
mkcert localhost 127.0.0.1 ::1
# скопировать в nginx/certs/ как certificate.crt и private.key
```

Конфигурация: `nginx/nginx.conf` — редирект 80→443, прокси `/` → frontend, `/api/`, `/auth/`, `/uploads/` → backend.

**Продакшн:** Let's Encrypt / Certbot на публичном домене; обновить `APP_FRONTEND_URL` и `VITE_API_BASE_URL` на `https://...`.

## 5. Мониторинг и обслуживание

- Healthcheck PostgreSQL в compose
- Логи: `docker compose logs [service]`
- Резервное копирование: volume `postgres_data` + каталог `backend/uploads`
- Swagger: `/swagger-ui.html` на backend

## 6. Безопасность

- Храните `JWT_SECRET` только в `.env`, не коммитьте
- Используйте сильный `POSTGRES_PASSWORD`
- В production: только HTTPS, ограничить CORS при необходимости
- Не публикуйте `GOOGLE_CLIENT_SECRET` в репозитории

## 7. Обновление версии

```bash
git pull
docker compose up --build -d
```

При изменении схемы БД — проверить `ddl-auto` или выполнить миграции вручную (см. [DDL](../03-database/ddl-scripts.md)).
