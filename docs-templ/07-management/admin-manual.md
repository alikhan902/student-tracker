# Руководство администратора — kanbaNice

## 1. Системные требования

| Компонент | Минимум | Рекомендуется |
|-----------|---------|---------------|
| CPU | 2 ядра | 4 ядра |
| RAM | 2 GB | 4 GB |
| Диск | 5 GB | 20 GB |
| ОС | Linux / Windows / macOS | Ubuntu 22.04 LTS |
| Docker | 24.0+ | 26.0+ |
| Docker Compose | 2.20+ | 2.27+ |

---

## 2. Развёртывание через Docker Compose

### 2.1. Клонирование репозитория

```bash
git clone https://github.com/firemon217/kanbaNice.git
cd kanbaNice
```

### 2.2. Настройка переменных окружения

Создайте файл `.env` в корне проекта:

```env
# PostgreSQL
POSTGRES_DB=kanbaNice
POSTGRES_USER=postgres
POSTGRES_PASSWORD=your_secure_password_here

# Backend
SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/kanbaNice
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=your_secure_password_here
FILE_UPLOAD_DIR=uploads
APP_FRONTEND_URL=https://your-domain.com/

# OAuth2 (если используется Google)
SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_GOOGLE_CLIENT_ID=your_client_id
SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_GOOGLE_CLIENT_SECRET=your_secret
```

### 2.3. Настройка SSL (опционально)

Поместите SSL-сертификат в папку `ssl/`:
```
ssl/
├── server.crt
└── server.key
```

Если SSL не используется, отредактируйте `nginx/nginx.conf` и отключите HTTPS-блок.

### 2.4. Запуск системы

```bash
docker-compose up --build -d
```

### 2.5. Проверка запуска

```bash
docker-compose ps
# Все сервисы должны быть в статусе "Up"

# Проверить логи backend
docker-compose logs backend

# Проверить доступность
curl http://localhost:8080/actuator/health
curl http://localhost/
```

---

## 3. Архитектура развёртывания

```
Internet
    │
    ▼
┌─────────────┐
│    Nginx    │  :80 / :443 (SSL)
│  (reverse   │  → раздаёт React SPA (dist/)
│   proxy)    │  → проксирует /api/* → backend:8080
└──────┬──────┘
       │
       ▼
┌─────────────┐
│   Backend   │  :8080 (только внутри Docker-сети)
│ Spring Boot │  → подключается к postgres:5432
└──────┬──────┘
       │
       ▼
┌─────────────┐
│ PostgreSQL  │  :5432 (только внутри Docker-сети)
│  (postgres  │  → данные в volume: postgres_data
│   volume)   │
└─────────────┘
```

---

## 4. Управление данными

### 4.1. Резервное копирование БД

```bash
docker exec kanbaNice-postgres pg_dump -U postgres kanbaNice > backup_$(date +%Y%m%d).sql
```

### 4.2. Восстановление из резервной копии

```bash
docker exec -i kanbaNice-postgres psql -U postgres kanbaNice < backup_20260601.sql
```

### 4.3. Подключение к БД напрямую

```bash
docker exec -it kanbaNice-postgres psql -U postgres -d kanbaNice
```

---

## 5. Управление контейнерами

| Команда | Описание |
|---------|---------|
| `docker-compose up -d` | Запустить все сервисы в фоне |
| `docker-compose down` | Остановить и удалить контейнеры |
| `docker-compose down -v` | Удалить контейнеры **и все данные** |
| `docker-compose restart backend` | Перезапустить только backend |
| `docker-compose logs -f backend` | Потоковый вывод логов backend |
| `docker-compose pull` | Обновить образы |

---

## 6. Обновление системы

```bash
git pull origin main
docker-compose down
docker-compose up --build -d
```

---

## 7. Мониторинг

### Проверка статуса сервисов

```bash
docker stats
```

### Логи

```bash
# Все сервисы
docker-compose logs

# Только ошибки backend за последний час
docker-compose logs --since=1h backend | grep ERROR
```

### Spring Boot Actuator

```
GET http://localhost:8080/actuator/health
GET http://localhost:8080/actuator/info
```

---

## 8. Безопасность

- **Обязательно** смените пароль PostgreSQL по умолчанию в `.env`
- Не коммитьте файл `.env` в репозиторий (добавлен в `.gitignore`)
- Для production настройте SSL-сертификаты (Let's Encrypt)
- Ограничьте порты 5432 и 8080 только внутри Docker-сети (текущая конфигурация)
- Регулярно обновляйте Docker-образы для получения патчей безопасности

---

## 9. Известные проблемы

| Проблема | Причина | Решение |
|---------|---------|---------|
| Backend не запускается | PostgreSQL не готов | Подождать 30 сек, проверить healthcheck |
| Nginx 502 Bad Gateway | Backend ещё стартует | Подождать 60 сек после `docker-compose up` |
| OAuth2 не работает | Не настроены Google credentials | Добавить client_id и secret в `.env` |
