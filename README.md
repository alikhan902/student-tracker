# Student Tracker — Руководство пользователя

Кратко: монорепозиторий содержит три компонента:

- `frontend/` — веб (Vite + React)
- `backend/` — сервис (Spring Boot)
- `desktop/` — десктопное приложение (Java/JavaFX)

---

## 1. Переменные окружения

В проекте используются файлы примеров с переменными окружения:

- Корневой пример: `.env.example`
- Примеры для бэкенда: `backend/.example`

Скопируйте `.env.example` в `.env` и заполните значения, например:

```
POSTGRES_DB=student_tracker
POSTGRES_USER=postgres
POSTGRES_PASSWORD=very_secret

SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/student_tracker
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=very_secret

FILE_UPLOAD_DIR=uploads
APP_FRONTEND_URL=http://localhost/

GOOGLE_CLIENT_ID=...
GOOGLE_CLIENT_SECRET=...

VITE_API_BASE_URL=http://localhost:8080
```

Для бэкенда также добавьте `JWT_SECRET` и, при использовании почты/Google OAuth, соответствующие ключи в `backend/.example`.

---

## 2. Запуск backend + frontend через Docker

В корне проекта есть `docker-compose.yml`, он поднимает `postgres`, `backend`, `frontend` и `nginx`.

Запуск (в корне репозитория):

```bash
# создать .env на основе .env.example и заполнить значения
cp .env.example .env
# запустить все сервисы в фоне
docker-compose up --build -d
# смотреть логи (например для backend)
docker-compose logs -f backend
```

Останов и удаление контейнеров:

```bash
docker-compose down
```

Порты по умолчанию (см. `docker-compose.yml`):

- Backend: `http://localhost:8080`
- Frontend (dev): `http://localhost:5173`
- Nginx: `http://localhost` (80), HTTPS (443)

---

## Настройка HTTPS

Коротко: для HTTPS используем `nginx` как обратный прокси, сертификаты располагаем в `nginx/certs`. Ниже — варианты для локальной разработки и для производства.

- Локальная разработка (рекомендуется `mkcert`):
  - Установите `mkcert` (https://github.com/FiloSottile/mkcert).
  - Выполните:

```bash
mkcert -install
mkcert localhost 127.0.0.1 ::1
```

  - Скопируйте полученные файлы (`localhost.pem` и `localhost-key.pem`) в `nginx/certs/` и переименуйте в `localhost.crt` и `localhost.key`.
  - Обновите `docker-compose.yml`/`nginx`-volumes, чтобы пробросить `nginx/certs` в контейнер, например:

```yaml
services:
  nginx:
    volumes:
      - ./nginx/nginx.conf:/etc/nginx/nginx.conf:ro
      - ./nginx/certs:/etc/nginx/certs:ro
```

  - В `nginx/nginx.conf` добавьте или включите блок HTTPS:

```nginx
server {
  listen 443 ssl;
  server_name localhost;

  ssl_certificate /etc/nginx/certs/localhost.crt;
  ssl_certificate_key /etc/nginx/certs/localhost.key;

  location / {
    proxy_pass http://backend:8080; # пример
    proxy_set_header Host $host;
    proxy_set_header X-Real-IP $remote_addr;
  }
}
```

- Самоподписанный сертификат (если не хотите `mkcert`):

```bash
openssl req -x509 -nodes -days 365 -newkey rsa:2048 \
  -keyout nginx/certs/localhost.key -out nginx/certs/localhost.crt \
  -subj "/CN=localhost"
```

- Продакшн (Let's Encrypt / Certbot или автоматические прокси типа Traefik):
  - Для публичного домена используйте `certbot` или интеграцию через `nginx-proxy`/`traefik`.
  - Настройте проброс портов 80/443 и автоматическое обновление сертификатов (Certbot + cron/systemd timer либо встроенный ACME в Traefik).

- Обновите переменные окружения для HTTPS:
  - В `.env` и `backend/.example` укажите `APP_FRONTEND_URL=https://your-domain` и `VITE_API_BASE_URL=https://your-domain` (или `https://localhost` для локали).

Примечание: в локальной разработке браузер может блокировать небезопасные certs — `mkcert` упрощает доверие сертификатам на машине разработчика.

## 3. Запуск десктопа (локально)

Требования: Java 17+, Maven.

Сборка и запуск:

```bash
cd desktop
./mvnw clean package
java -jar target/*.jar
```

Для запуска из Maven (JavaFX):

```bash
./mvnw javafx:run
```

---

## 4. REST API — полный список эндпоинтов

Обратите внимание: большинство защищённых эндпоинтов ожидают авторизации. Логин возвращает `accessToken` (JWT):

- Передавайте заголовок `Authorization: Bearer <token>` для защищённых запросов.

Список (метод — путь — описание):

- Auth
  - `POST /auth/login` — вход. Тело: `LoginRequestDTO` (email/username + password). Ответ: `{"accessToken":"...","userId":123}`
  - `POST /auth/signup` — регистрация. Тело: `SignupRequestDTO`. Ответ: `{ "id":..., "username":... }`

- Users (`/api/users`)
  - `GET /api/users/profile` — получить профиль текущего пользователя
  - `PUT /api/users/profile` — обновить профиль. Тело: `UpdateProfileDto`
  - `PUT /api/users/change-password` — сменить пароль. Тело: `ChangePasswordDto`
  - `POST /api/users/forgot-password?email=...` — запустить восстановление пароля (по email)
  - `POST /api/users/reset-password?token=...&newPassword=...` — сброс пароля по токену
  - `DELETE /api/users/delete` — удалить аккаунт (опциональный `password` параметр)
  - `GET /api/users/all` — получить список всех пользователей

- Groups (`/api/groups`)
  - `POST /api/groups` — создать группу. Тело: `GroupDTO`
  - `GET /api/groups/my-group` — получить группу текущего пользователя
  - `PUT /api/groups` — обновить группу. Тело: `GroupDTO`
  - `DELETE /api/groups` — удалить группу текущего пользователя
  - `PUT /api/groups/add-student/{username}` — добавить студента по логину
  - `DELETE /api/groups/delete-student/{id}` — удалить студента по id

- Training Subjects (`/api/training-subjects`)
  - `POST /api/training-subjects` — создать предмет обучения. Content-Type: `multipart/form-data`. Части:
    - `data` — JSON строка с `TrainingSubjectUploadDTO`
    - `photo` — (опционально) файл изображения
  - `PUT /api/training-subjects/{id}` — обновить предмет (multipart как выше)
  - `GET /api/training-subjects/{id}` — получить предмет по id
  - `GET /api/training-subjects` — список предметов для группы
  - `DELETE /api/training-subjects/{id}` — удалить предмет

- Educational Materials (`/api/educational-materials`)
  - `POST /api/educational-materials` — загрузить материал (multipart/form-data):
    - `data` — JSON строка с `EducationalMaterialUploadDTO`
    - `file` — (опционально) прикреплённый файл (.pdf/.txt/и т.п.)
  - `PUT /api/educational-materials/{id}` — обновить материал (multipart как выше)
  - `GET /api/educational-materials/{id}` — получить материал по id
  - `GET /api/educational-materials/subject/{id}` — список материалов по `trainingSubjectId`
  - `DELETE /api/educational-materials/{id}` — удалить материал

- Material Completion (`/api/material-complete`)
  - `PUT /api/material-complete/{materialId}` — создать/обновить статус выполнения материала для текущего пользователя. Тело: `MaterialCompleteUpdateDTO`
  - `GET /api/material-complete/{materialId}` — получить статус выполнения для текущего пользователя

---

## 5. Примеры multipart-запроса (curl)

Пример создания предмета с JSON-частью и фото:

```bash
curl -X POST "http://localhost:8080/api/training-subjects" \
  -H "Authorization: Bearer $TOKEN" \
  -F "data={\"title\":\"Математика\",\"description\":\"...\"};type=application/json" \
  -F "photo=@/path/to/photo.png"
```

Пример загрузки материала:

```bash
curl -X POST "http://localhost:8080/api/educational-materials" \
  -H "Authorization: Bearer $TOKEN" \
  -F "data={\"title\":\"Лекция 1\",\"trainingSubjectId\":1};type=application/json" \
  -F "file=@/path/to/file.pdf"
```

---

## 6. Полезные файлы в репозитории

- `.env.example` — пример корневых переменных окружения
- `backend/.example` — пример переменных для бэкенда (JWT_SECRET, mail и т.д.)
- `docker-compose.yml` — конфигурация для локального запуска всех сервисов
- Документация: `docs/` (frontend/backend/desktop)

---

Если хотите, могу: сгенерировать полноценную OpenAPI-спецификацию по классам контроллеров, дополнить примеры DTO (формат полей) или добавить `CONTRIBUTING.md` и шаблоны PR/ISSUE.