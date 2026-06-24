# Спецификация интерфейсов между слоями PCMEF

## Control → Mediator

### IUserService (`UserService`)

| Метод | Описание |
|-------|----------|
| `getProfile()` | Профиль текущего пользователя |
| `updateProfile(UpdateProfileDto)` | Обновление имени и др. |
| `changePassword(ChangePasswordDto)` | Смена пароля (LOCAL/EMAIL) |
| `forgotPassword(email)` | Токен сброса + email |
| `resetPassword(token, newPassword)` | Установка нового пароля |
| `deleteAccount(password?)` | Удаление аккаунта |

**Использует:** `UserController`

---

### IGroupService (`GroupServiceImpl`)

| Метод | Описание |
|-------|----------|
| `createGroup(GroupDTO)` | Только HEADMAN |
| `getMyGroup()` | Группа текущего пользователя |
| `updateGroup(GroupDTO)` | Переименование |
| `deleteGroup()` | Удаление группы старостой |
| `addStudent(username)` | Добавление STANDARD |
| `deleteStudent(id)` | Исключение из группы |

**Использует:** `GroupController`

---

### ITrainingSubjectService (`TrainingSubjectServiceImpl`)

| Метод | Описание |
|-------|----------|
| `create(MultipartFile photo, TrainingSubjectUploadDTO)` | Создание с фото |
| `update(id, ...)` | Обновление |
| `getById(id)` | Один предмет |
| `listForCurrentGroup()` | Список группы |
| `delete(id)` | Удаление |

**Использует:** `TrainingSubjectController`

---

### IEducationalMaterialService (`EducationalMaterialServiceImpl`)

| Метод | Описание |
|-------|----------|
| `create(file, dto)` | Загрузка материала |
| `update(id, file, dto)` | Обновление |
| `getById(id)` | Детали |
| `listBySubject(subjectId)` | Список по предмету |
| `delete(id)` | Удаление |

**Использует:** `EducationalMaterialController`

---

### IMaterialCompleteService (`MaterialCompleteServiceImpl`)

| Метод | Описание |
|-------|----------|
| `upsert(materialId, dto)` | Создать/обновить статус |
| `getForCurrentUser(materialId)` | Статус текущего пользователя |

**Использует:** `MaterialCompleteController`

---

## Mediator → Foundation (Spring Data)

| Repository | Сущность |
|------------|----------|
| `UserRepository` | `User` |
| `GroupRepository` | `Group` |
| `TrainingSubjectRepository` | `TrainingSubject` |
| `EducationalMaterialRepository` | `EducationalMaterial` |
| `MaterialCompleteRepository` | `MaterialComplete` |

## Presentation → Control (REST)

Базовые пути:
- `/auth` — публичная аутентификация
- `/api/users`, `/api/groups`, `/api/training-subjects`, `/api/educational-materials`, `/api/material-complete` — с JWT
- `/uploads/**` — статика файлов (частично публичная)

Полный перечень — в [техническом задании](../07-management/technical-specification.md#4-api) и Swagger UI (`/swagger-ui.html`).
