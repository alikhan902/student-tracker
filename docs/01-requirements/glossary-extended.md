# Расширенный глоссарий (системный уровень)

| № | Термин | Определение | Реализация |
|---|--------|------------|------------|
| 1 | Group | Учебная группа | `entity/Group`, таблица `app_group` |
| 2 | User | Учётная запись | `entity/User`, `app_user` |
| 3 | TrainingSubject | Учебный предмет | `entity/TrainingSubject` |
| 4 | EducationalMaterial | Материал с файлом | `entity/EducationalMaterial` |
| 5 | MaterialComplete | Статус выполнения | `entity/MaterialComplete`, `MaterialCompleteId` |
| 6 | HEADMAN | Староста | `StudentType.HEADMAN` |
| 7 | STANDARD | Обычный студент | `StudentType.STANDARD` |
| 8 | DTO | Объект передачи данных | пакет `dto/` |
| 9 | JwtAuthFilter | Фильтр Bearer-токена | `Security/JwtAuthFilter` |
| 10 | BackendApi | HTTP-клиент desktop | `desktop/.../BackendApi.java` |
| 11 | FileService | Сохранение uploads | `FileServiceImpl` |
| 12 | ModelMapper | Entity ↔ DTO | Spring bean, сервисный слой |
