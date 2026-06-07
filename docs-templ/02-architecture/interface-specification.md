# Спецификация интерфейсов между слоями PCMEF

## Control → Mediator (IService-интерфейсы)

### IUserService
```java
public interface IUserService {
    UserProfileResponseDTO getProfile(Long userId);
    UserProfileResponseDTO updateProfile(Long userId, UpdateProfileDto dto);
    String forgotPassword(String email);
    String resetPassword(String token, String newPassword);
    String deleteAccount(Long userId);
}
```
**Реализация:** `UserService`  
**Использует:** `UserController`

---

### ICompanyService
```java
public interface ICompanyService {
    CompanyResponseDTO getMyCompany();
    CompanyResponseDTO createCompany(CompanyCreateDTO dto);
    CompanyResponseDTO updateMyCompany(CompanyUpdateDTO dto);
    void deleteMyCompany();
    void addWorkerToMyCompany(WorkerRequestDTO dto);
    void deleteWorkerToMyCompany(Long workerId);
}
```
**Реализация:** `CompanyService`  
**Использует:** `CompanyController`

---

### IProjectService
```java
public interface IProjectService {
    List<ProjectResponseDTO> listMyProjects();
    ProjectResponseDTO createProject(ProjectCreateDTO dto);
    ProjectResponseDTO getProject(Long projectId);
    void deleteProject(Long projectId);
    ProjectResponseDTO addWorkerToProject(Long projectId, Long userId);
}
```
**Реализация:** `ProjectService`  
**Использует:** `ProjectController`

---

### IBoardService
```java
public interface IBoardService {
    List<BoardResponseDTO> listBoards(Long projectId);
    BoardResponseDTO createBoard(Long projectId, BoardCreateDTO dto);
    BoardResponseDTO updateBoard(Long boardId, BoardUpdateDTO dto);
    void deleteBoard(Long boardId);
}
```
**Реализация:** `BoardService`  
**Использует:** `BoardController`

---

### ITaskService
```java
public interface ITaskService {
    List<TaskResponseDTO> listTasks(Long boardId);
    TaskResponseDTO createTask(Long boardId, TaskCreateDTO dto);
    TaskResponseDTO getTask(Long taskId);
    TaskResponseDTO updateTask(Long taskId, TaskUpdateDTO dto);
    void deleteTask(Long taskId);
}
```
**Реализация:** `TaskService`  
**Использует:** `TaskController`

---

## Mediator → Foundation (Repository-интерфейсы)

### UserRepository
```java
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByResetToken(String resetToken);
}
```

### KanbanProjectRepository
```java
public interface KanbanProjectRepository extends JpaRepository<KanbanProject, Long> {
    List<KanbanProject> findByCompanyId(Long companyId);
}
```

### KanbanBoardRepository
```java
public interface KanbanBoardRepository extends JpaRepository<KanbanBoard, Long> {
    List<KanbanBoard> findByProjectId(Long projectId);
}
```

### KanbanTaskRepository
```java
public interface KanbanTaskRepository extends JpaRepository<KanbanTask, Long> {
    List<KanbanTask> findByBoardId(Long boardId);
}
```

### ProjectMemberRepository
```java
public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {
    Optional<ProjectMember> findByProjectIdAndUserId(Long projectId, Long userId);
    List<ProjectMember> findByProjectId(Long projectId);
}
```

---

## DTO-контракты (Control ↔ клиент)

| DTO | Направление | Поля |
|-----|------------|------|
| `LoginRequestDTO` | Клиент → Сервер | username, password |
| `LoginResponseDTO` | Сервер → Клиент | token |
| `SignupRequestDTO` | Клиент → Сервер | name, username, email, password |
| `SignupResponseDTO` | Сервер → Клиент | id, username, email |
| `TaskCreateDTO` | Клиент → Сервер | title, description |
| `TaskUpdateDTO` | Клиент → Сервер | title, description, status |
| `TaskResponseDTO` | Сервер → Клиент | id, title, description, status, boardId, createdAt |
| `ProjectCreateDTO` | Клиент → Сервер | name |
| `ProjectResponseDTO` | Сервер → Клиент | id, name, companyId, members, boards, createdAt |
| `CompanyCreateDTO` | Клиент → Сервер | name |
| `CompanyResponseDTO` | Сервер → Клиент | id, name, workers |
