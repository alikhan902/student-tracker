# Спецификация ключевых методов

## AuthService.login

| Поле | Значение |
|------|----------|
| Класс | `AuthService` |
| Вход | `LoginRequestDTO` (username, password) |
| Выход | `LoginResponseDTO` (accessToken, userId) |
| Pre | Пользователь существует, пароль BCrypt |
| Post | JWT в ответе, `SecurityContext` установлен на время запроса |

**Алгоритм:** `authenticationManager.authenticate` → загрузка User → `AuthUtil.generateToken(user)`.

---

## GroupServiceImpl.createGroup

| Поле | Значение |
|------|----------|
| Вход | `GroupDTO` (name) |
| Выход | `GroupResponseDTO` |
| Pre | `studentType == HEADMAN`, группы ещё нет |
| Post | `User.group` установлен |

**Исключения:** `AccessDeniedException`, `IllegalStateException` при повторном создании.

---

## TrainingSubjectServiceImpl.create

| Поле | Значение |
|------|----------|
| Вход | `TrainingSubjectUploadDTO`, `MultipartFile photo` |
| Pre | Пользователь в группе, HEADMAN или валидация доступа |
| Post | `photo_path` заполнен при наличии файла |

**Зависимости:** `FileService.saveFile("training-subjects", ...)`.

---

## MaterialCompleteServiceImpl.upsert

| Поле | Значение |
|------|----------|
| Вход | `materialId`, `MaterialCompleteUpdateDTO` (status) |
| Pre | Материал принадлежит группе пользователя |
| Post | Запись в `material_complete` с PK (userId, materialId) |

**Идемпотентность:** повторный PUT обновляет тот же статус.

---

## FileServiceImpl.saveFile

| Поле | Значение |
|------|----------|
| Вход | directory, originalFilename, bytes |
| Выход | относительный URL `/uploads/{directory}/{uuid}_name` |
| Pre | каталог создаётся при отсутствии |
| Post | файл на диске, путь в DTO/entity |
