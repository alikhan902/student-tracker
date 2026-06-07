# Матрица трассировки требований

## Бизнес-прецеденты → Системные прецеденты

| Бизнес-прецедент | Системный UC | API-эндпоинт | Сущность | Статус |
|-----------------|--------------|-------------|---------|--------|
| Регистрация и вход | UC-01 | POST /auth/signup | User | ✅ |
| | UC-02 | POST /auth/login | User | ✅ |
| | UC-03 | /oauth2/** | User | ✅ |
| | UC-04 | POST /api/users/forgot-password | User | ✅ |
| | UC-05 | POST /api/users/reset-password | User | ✅ |
| Профиль | UC-06 | GET /api/users/profile | User | ✅ |
| | UC-07 | PUT /api/users/profile | User | ✅ |
| | UC-08 | DELETE /api/users/delete | User | ✅ |
| Группа | UC-09 | POST /api/groups | Group | ✅ |
| | UC-10 | GET /api/groups/my-group | Group | ✅ |
| | UC-11 | PUT /api/groups | Group | ✅ |
| | UC-12 | DELETE /api/groups | Group | ✅ |
| Состав группы | UC-13 | PUT /api/groups/add-student/{username} | User, Group | ✅ |
| | UC-14 | DELETE /api/groups/delete-student/{id} | User | ✅ |
| Предметы | UC-15 | GET /api/training-subjects | TrainingSubject | ✅ |
| | UC-16 | POST /api/training-subjects | TrainingSubject | ✅ |
| | UC-17 | GET /api/training-subjects/{id} | TrainingSubject | ✅ |
| | UC-18 | PUT /api/training-subjects/{id} | TrainingSubject | ✅ |
| | UC-19 | DELETE /api/training-subjects/{id} | TrainingSubject | ✅ |
| Материалы | UC-20 | POST /api/educational-materials | EducationalMaterial | ✅ |
| | UC-21 | GET /api/educational-materials/subject/{id} | EducationalMaterial | ✅ |
| | UC-22 | PUT /api/educational-materials/{id} | EducationalMaterial | ✅ |
| | UC-23 | DELETE /api/educational-materials/{id} | EducationalMaterial | ✅ |
| Выполнение | UC-24 | PUT /api/material-complete/{materialId} | MaterialComplete | ✅ |
| | UC-25 | GET /api/material-complete/{materialId} | MaterialComplete | ✅ |

## Системные требования → Компоненты

| Требование | Слой | Класс |
|------------|------|-------|
| JWT | Security | `JwtAuthFilter`, `AuthUtil`, `AuthService` |
| OAuth2 | Security | `OAuth2successHandler`, `SecurityConfig` |
| BCrypt | Security | `SecurityConfig` |
| CRUD группы | C/M/F | `GroupController`, `GroupServiceImpl`, `GroupRepository` |
| Multipart upload | M | `FileServiceImpl`, `TrainingSubjectServiceImpl` |
| Email сброса | M | `EmailServiceImpl`, `UserService` |
| Swagger | Config | springdoc OpenAPI |
| CORS / uploads | Config | `WebConfig` |
