# Реализация ядра системы

## Статус по слоям PCMEF

| Слой | Статус | Компоненты |
|------|--------|------------|
| Foundation (F) | ✅ | `UserRepository`, `GroupRepository`, `TrainingSubjectRepository`, `EducationalMaterialRepository`, `MaterialCompleteRepository` |
| Entity (E) | ✅ | `User`, `Group`, `TrainingSubject`, `EducationalMaterial`, `MaterialComplete` + enums |
| Mediator (M) | ✅ | `AuthService`, `UserService`, `GroupServiceImpl`, `TrainingSubjectServiceImpl`, `EducationalMaterialServiceImpl`, `MaterialCompleteServiceImpl`, `FileServiceImpl`, `EmailServiceImpl` |
| Control (C) | ✅ | 6 REST-контроллеров + `GlobalExceptionHandler` |
| Presentation (P) | ✅ | React: login, signup, profile, subjects, groups; Desktop: эквивалентные экраны в `App.java` |

## Реализованные Use Cases

| UC | Сервис / компонент | Статус |
|----|-------------------|--------|
| UC-01 Регистрация | `AuthService.signup` | ✅ |
| UC-02 Вход | `AuthService.login` | ✅ |
| UC-03 OAuth2 | `OAuth2successHandler` | ✅ |
| UC-04..05 Сброс пароля | `UserService` | ✅ |
| UC-06..08 Профиль | `UserService` | ✅ |
| UC-09..14 Группа | `GroupServiceImpl` | ✅ |
| UC-15..19 Предметы | `TrainingSubjectServiceImpl` | ✅ |
| UC-20..23 Материалы | `EducationalMaterialServiceImpl` | ✅ |
| UC-24..25 Выполнение | `MaterialCompleteServiceImpl` | ✅ |

## Локальный запуск

### Docker (рекомендуется)

```bash
cp .env.example .env
docker compose up --build -d
```

Порты: backend `8080`, frontend `5173`, nginx `80`/`443`.

### Backend отдельно

```bash
cd backend
./mvnw clean package
java -jar target/*.jar
```

Требования: Java 17+, PostgreSQL, переменные из `backend/.example` (`JWT_SECRET`, datasource, mail, OAuth).

### Frontend отдельно

```bash
cd frontend
npm install
npm run dev
```

### Desktop

```bash
cd desktop
./mvnw clean package
java -jar target/*.jar
# или
./mvnw javafx:run
```

Базовый URL API: `http://localhost:8080` (переопределение: `-Dstudent.tracker.baseUrl=`).

## Переменные окружения

| Переменная | Назначение |
|------------|------------|
| `POSTGRES_*` | База данных |
| `SPRING_DATASOURCE_*` | JDBC для backend |
| `JWT_SECRET` / `jwt.secretKey` | Подпись JWT |
| `APP_FRONTEND_URL` | Redirect после OAuth |
| `GOOGLE_CLIENT_*`, GitHub OAuth | Внешний вход |
| `FILE_UPLOAD_DIR` | Каталог uploads |
| `VITE_API_BASE_URL` | База API для Vite |

## Покрытие тестами

| Модуль | Объём | Примечание |
|--------|-------|------------|
| Backend | ~21 test-классов, ~48% lines (JaCoCo) | Unit: entities, enums, `MaterialCompleteService`, mapping |
| Frontend | 8 Vitest-файлов | ~91% line coverage на протестированных компонентах |
| Desktop | 3 test-класса | Минимальный набор |

**МУ:** покрытие > 40% — выполняется за счёт backend + frontend unit-тестов.

**Отчёты:** [test-coverage-report.md](../06-implementation/test-coverage-report.md), [JaCoCo HTML](../06-implementation/jacoco/index.html).

## Swagger

После запуска backend: `http://localhost:8080/swagger-ui.html` (springdoc OpenAPI).

## Multipart (curl)

```bash
curl -X POST "http://localhost:8080/api/training-subjects" \
  -H "Authorization: Bearer $TOKEN" \
  -F 'data={"title":"Математика","description":"..."};type=application/json' \
  -F "photo=@/path/to/photo.png"
```
