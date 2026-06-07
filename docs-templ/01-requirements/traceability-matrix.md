# Матрица трассировки требований

## Бизнес-прецеденты → Системные прецеденты

| Бизнес-прецедент | Системный прецедент | API-эндпоинт | Сущность | Реализован |
|-----------------|---------------------|-------------|---------|-----------|
| UC-B1 Регистрация и вход | UC-01 Регистрация | POST /auth/signup | User | ✅ |
| UC-B1 Регистрация и вход | UC-02 Вход (пароль) | POST /auth/login | User | ✅ |
| UC-B1 Регистрация и вход | UC-03 Вход (Google) | OAuth2 | User | ✅ |
| UC-B1 Регистрация и вход | UC-04 Запрос сброса пароля | POST /api/users/forgot-password | User | ✅ |
| UC-B1 Регистрация и вход | UC-05 Сброс пароля | POST /api/users/reset-password | User | ✅ |
| UC-B1 Регистрация и вход | UC-06 Просмотр профиля | GET /api/users/profile | User | ✅ |
| UC-B1 Регистрация и вход | UC-07 Редактирование профиля | PUT /api/users/profile | User | ✅ |
| UC-B1 Регистрация и вход | UC-08 Удаление аккаунта | DELETE /api/users/profile | User | ✅ |
| UC-B2 Управление компанией | UC-09 Создать компанию | POST /api/company | Company | ✅ |
| UC-B2 Управление компанией | UC-10 Просмотр компании | GET /api/company | Company | ✅ |
| UC-B2 Управление компанией | UC-11 Обновить компанию | PUT /api/company | Company | ✅ |
| UC-B2 Управление компанией | UC-12 Удалить компанию | DELETE /api/company | Company | ✅ |
| UC-B3 Управление сотрудниками | UC-13 Добавить сотрудника | POST /api/company/workers | User, Company | ✅ |
| UC-B3 Управление сотрудниками | UC-14 Удалить сотрудника | DELETE /api/company/workers/{id} | User, Company | ✅ |
| UC-B4 Ведение проектов | UC-15 Список проектов | GET /api/projects | KanbanProject | ✅ |
| UC-B4 Ведение проектов | UC-16 Создать проект | POST /api/projects | KanbanProject | ✅ |
| UC-B4 Ведение проектов | UC-17 Просмотр проекта | GET /api/projects/{id} | KanbanProject | ✅ |
| UC-B4 Ведение проектов | UC-18 Удалить проект | DELETE /api/projects/{id} | KanbanProject | ✅ |
| UC-B4 Ведение проектов | UC-19 Добавить участника | POST /api/projects/{id}/members/{userId} | ProjectMember | ✅ |
| UC-B5 Управление досками | UC-20 Список досок | GET /api/projects/{id}/boards | KanbanBoard | ✅ |
| UC-B5 Управление досками | UC-21 Создать доску | POST /api/projects/{id}/boards | KanbanBoard | ✅ |
| UC-B5 Управление досками | UC-22 Обновить доску | PUT /api/projects/boards/{boardId} | KanbanBoard | ✅ |
| UC-B5 Управление досками | UC-23 Удалить доску | DELETE /api/projects/boards/{boardId} | KanbanBoard | ✅ |
| UC-B6 Работа с задачами | UC-24 Список задач | GET /api/projects/boards/{boardId}/tasks | KanbanTask | ✅ |
| UC-B6 Работа с задачами | UC-25 Создать задачу | POST /api/projects/boards/{boardId}/tasks | KanbanTask | ✅ |
| UC-B6 Работа с задачами | UC-26 Просмотр задачи | GET /api/projects/tasks/{taskId} | KanbanTask | ✅ |
| UC-B6 Работа с задачами | UC-27 Обновить задачу | PUT /api/projects/tasks/{taskId} | KanbanTask | ✅ |
| UC-B6 Работа с задачами | UC-28 Удалить задачу | DELETE /api/projects/tasks/{taskId} | KanbanTask | ✅ |

## Системные требования → Компоненты реализации

| Системное требование | Компонент (слой) | Класс / Файл |
|---------------------|-----------------|-------------|
| JWT-аутентификация | Security | JwtAuthFilter, AuthService |
| OAuth2 (Google) | Security | OAuth2successHandler, SecurityConfig |
| Хеширование паролей | Security | SecurityConfig (BCryptPasswordEncoder) |
| Управление ролями | Entity, Security | RoleType, RolePermissionMapping |
| CRUD компании | Control, Mediator, Foundation | CompanyController, CompanyService, UserRepository |
| CRUD проектов | Control, Mediator, Foundation | ProjectController, ProjectService, KanbanProjectRepository |
| CRUD досок | Control, Mediator, Foundation | BoardController, BoardService, KanbanBoardRepository |
| CRUD задач | Control, Mediator, Foundation | TaskController, TaskService, KanbanTaskRepository |
| Email сброса пароля | Mediator | UserService, EmailServiceImpl |
| Swagger документация | Config | SwaggerConfig, SpringDoc OpenAPI |
| CORS настройка | Config | WebConfig |
| Контейнеризация | Инфраструктура | docker-compose.yml, Dockerfile (backend/frontend/nginx) |
| SSL/HTTPS | Инфраструктура | nginx.conf |
| Веб-интерфейс | Presentation | React pages/, components/ |
| Десктоп-интерфейс | Presentation | JavaFX view/ |
