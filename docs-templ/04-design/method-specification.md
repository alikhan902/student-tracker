# Спецификация ключевых методов

## AuthService

### `login(LoginRequestDTO dto) : LoginResponseDTO`
| Поле | Значение |
|------|----------|
| **Слой** | Mediator |
| **Предусловие** | `dto.username` и `dto.password` не пустые |
| **Алгоритм** | 1. Найти User по username через UserRepository<br>2. Проверить пароль через BCryptPasswordEncoder.matches()<br>3. Сгенерировать JWT с ролями пользователя<br>4. Вернуть LoginResponseDTO {token} |
| **Исключения** | `BadCredentialsException` (401) — неверный пароль или пользователь не найден |
| **Возвращает** | `LoginResponseDTO` с JWT-токеном |

---

### `SignUp(SignupRequestDTO dto) : SignupResponseDTO`
| Поле | Значение |
|------|----------|
| **Слой** | Mediator |
| **Предусловие** | Email и username уникальны |
| **Алгоритм** | 1. Проверить уникальность username и email<br>2. Хешировать пароль через BCrypt<br>3. Создать User с ролью USER и providerType=LOCAL<br>4. Сохранить через UserRepository<br>5. Вернуть SignupResponseDTO |
| **Исключения** | `BadRequestException` (400) — username или email уже заняты |
| **Возвращает** | `SignupResponseDTO` с id и username |

---

## TaskService

### `createTask(Long boardId, TaskCreateDTO dto) : TaskResponseDTO`
| Поле | Значение |
|------|----------|
| **Слой** | Mediator |
| **Предусловие** | boardId существует в БД |
| **Алгоритм** | 1. Найти KanbanBoard по boardId<br>2. Создать KanbanTask с status=TODO и связать с board<br>3. Сохранить через KanbanTaskRepository (createdAt устанавливается @PrePersist)<br>4. Смаппировать в TaskResponseDTO через ModelMapper |
| **Исключения** | `ResourceNotFoundException` (404) — доска не найдена |
| **Возвращает** | `TaskResponseDTO` с id, title, description, status=TODO |

---

### `updateTask(Long taskId, TaskUpdateDTO dto) : TaskResponseDTO`
| Поле | Значение |
|------|----------|
| **Слой** | Mediator |
| **Предусловие** | taskId существует |
| **Алгоритм** | 1. Найти KanbanTask по taskId<br>2. Обновить title, description, status из dto<br>3. updatedAt устанавливается @PreUpdate<br>4. Сохранить и вернуть DTO |
| **Исключения** | `ResourceNotFoundException` (404) |

---

## CompanyService

### `addWorkerToMyCompany(WorkerRequestDTO dto) : void`
| Поле | Значение |
|------|----------|
| **Слой** | Mediator |
| **Предусловие** | Текущий пользователь — ADMIN с привязанной компанией; worker найден по email |
| **Алгоритм** | 1. Получить текущего пользователя через CurrentUserUtil<br>2. Найти сотрудника по email через UserRepository<br>3. Установить worker.company = admin.company<br>4. Сохранить worker через UserRepository |
| **Исключения** | `ResourceNotFoundException` (404) — сотрудник не найден |

---

## ProjectService

### `addWorkerToProject(Long projectId, Long userId) : ProjectResponseDTO`
| Поле | Значение |
|------|----------|
| **Слой** | Mediator |
| **Предусловие** | Проект и пользователь существуют; пользователь ещё не участник |
| **Алгоритм** | 1. Найти KanbanProject и User<br>2. Создать ProjectMember с role=WORKER<br>3. Сохранить через ProjectMemberRepository<br>4. Вернуть обновлённый ProjectResponseDTO |
| **Исключения** | `ResourceNotFoundException` (404), `BadRequestException` (400) — уже участник |

---

## UserService

### `forgotPassword(String email) : String`
| Поле | Значение |
|------|----------|
| **Слой** | Mediator |
| **Алгоритм** | 1. Найти User по email<br>2. Сгенерировать UUID reset-токен<br>3. Установить resetToken и resetTokenExpiry (+1 час)<br>4. Сохранить User<br>5. Отправить email со ссылкой через EmailService |
| **Rate limit** | Не чаще одного запроса в 1 минуту (lastPasswordResetRequest) |
| **Возвращает** | Строка-подтверждение |
