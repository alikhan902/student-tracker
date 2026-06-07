# Реализация ядра системы

## Статус реализации по слоям PCMEF

| Слой | Статус | Классы |
|------|--------|--------|
| Foundation (F) | ✅ Полностью | UserRepository, KanbanProjectRepository, KanbanBoardRepository, KanbanTaskRepository, KanbaniceUserCompanyRepository, ProjectMemberRepository |
| Entity (E) | ✅ Полностью | User, Company, KanbanProject, KanbanBoard, KanbanTask, ProjectMember + перечисления |
| Mediator (M) | ✅ Полностью | AuthService, UserService, CompanyService, ProjectService, BoardService, TaskService, EmailServiceImpl |
| Control (C) | ✅ Полностью | AuthController, UserController, CompanyController, ProjectController, BoardController, TaskController |
| Presentation (P) | ✅ Полностью | 7 страниц React + 7 экранов JavaFX |

## Реализованные Use Cases

| Use Case | Сервис | Статус |
|----------|--------|--------|
| UC-01 Регистрация | AuthService.SignUp() | ✅ |
| UC-02 Вход | AuthService.login() | ✅ |
| UC-03 OAuth2 Google | OAuth2successHandler | ✅ |
| UC-04/05 Сброс пароля | UserService.forgotPassword/resetPassword | ✅ |
| UC-06..08 Профиль | UserService | ✅ |
| UC-09..14 Компания | CompanyService | ✅ |
| UC-15..19 Проекты | ProjectService | ✅ |
| UC-20..23 Доски | BoardService | ✅ |
| UC-24..28 Задачи | TaskService | ✅ |

## Покрытие тестами

> **Текущее состояние:** 93 теста написаны и проходят. Покрытие **70.9% строк** (требование >40% выполнено).

**Требование МУ:** покрытие > 40% (JUnit + JaCoCo)

### Рекомендуемые тесты для реализации

```java
// Пример — AuthServiceTest (Mediator слой)
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock UserRepository userRepository;
    @Mock PasswordEncoder passwordEncoder;
    @InjectMocks AuthService authService;

    @Test
    void login_validCredentials_returnsToken() {
        User user = User.builder()
            .username("test").password("hashed").roles(Set.of(RoleType.USER)).build();
        when(userRepository.findByUsername("test")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("raw", "hashed")).thenReturn(true);

        LoginResponseDTO result = authService.login(new LoginRequestDTO("test", "raw"));
        assertNotNull(result.getToken());
    }

    @Test
    void login_wrongPassword_throwsBadCredentials() {
        // ...
    }
}
```

```java
// Пример — TaskServiceTest (Mediator слой)
@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock KanbanTaskRepository taskRepository;
    @Mock KanbanBoardRepository boardRepository;
    @InjectMocks TaskService taskService;

    @Test
    void createTask_validBoard_returnsTask() {
        KanbanBoard board = KanbanBoard.builder().id(1L).name("Sprint 1").build();
        when(boardRepository.findById(1L)).thenReturn(Optional.of(board));

        TaskCreateDTO dto = new TaskCreateDTO("Сделать задачу", "Описание");
        TaskResponseDTO result = taskService.createTask(1L, dto);

        assertEquals("Сделать задачу", result.getTitle());
        assertEquals(TaskStatus.TODO, result.getStatus());
    }

    @Test
    void createTask_boardNotFound_throwsNotFound() {
        when(boardRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
            () -> taskService.createTask(99L, new TaskCreateDTO("T", "D")));
    }
}
```

### Приоритеты тестирования

1. `AuthService` — критичный для безопасности
2. `TaskService` — ключевая бизнес-логика
3. `CompanyService` — управление данными
4. `ProjectService` — сложные связи между сущностями
5. `GlobalExceptionHandler` — корректность HTTP-статусов

## Структура тестовых директорий (для реализации)

```
backend/src/test/java/com/kanbanice/backend/
├── service/
│   ├── AuthServiceTest.java
│   ├── UserServiceTest.java
│   ├── CompanyServiceTest.java
│   ├── ProjectServiceTest.java
│   ├── BoardServiceTest.java
│   └── TaskServiceTest.java
├── controller/
│   ├── AuthControllerTest.java
│   └── ...
└── repository/
    └── UserRepositoryTest.java (интеграционный)
```
