# Паттерны рефакторинга

## Data Mapper (Обязательный)

### Описание
Паттерн **Data Mapper** разделяет бизнес-объекты (Entity) от объектов передачи данных (DTO), обеспечивая независимость слоёв. В kanbaNice реализован через **ModelMapper** в сервисном слое.

### Реализация в проекте

```java
// Зависимость в pom.xml:
// <dependency>modelmapper 3.2.0</dependency>

// В каждом сервисе:
@Service
@RequiredArgsConstructor
public class TaskService {

    private final KanbanTaskRepository taskRepository;
    private final KanbanBoardRepository boardRepository;
    private final ModelMapper modelMapper;  // <-- Data Mapper

    public TaskResponseDTO createTask(Long boardId, TaskCreateDTO dto) {
        KanbanBoard board = boardRepository.findById(boardId)
            .orElseThrow(() -> new ResourceNotFoundException("Board not found"));

        KanbanTask task = KanbanTask.builder()
            .title(dto.getTitle())
            .description(dto.getDescription())
            .status(TaskStatus.TODO)
            .board(board)
            .build();

        KanbanTask saved = taskRepository.save(task);

        // Data Mapper: Entity → DTO (отдельный объект, не сама сущность)
        return modelMapper.map(saved, TaskResponseDTO.class);
    }
}
```

### Что достигается
- **Entity** (`KanbanTask`) не знает о структуре API и не раскрывает лишние поля
- **DTO** (`TaskResponseDTO`) содержит только то, что нужно клиенту
- Изменение структуры API не требует правки Entity-класса

---

## Identity Map (Обязательный)

### Описание
Паттерн **Identity Map** гарантирует, что в рамках одного сеанса каждый объект с данным идентификатором существует в единственном экземпляре. Предотвращает дублирование запросов к БД.

### Реализация в проекте

В kanbaNice Identity Map обеспечивается **Hibernate Session** как часть механизма JPA:

```java
// Hibernate автоматически ведёт Identity Map в рамках транзакции:
// Первый вызов → запрос к БД
KanbanBoard board1 = boardRepository.findById(1L).get(); // SELECT FROM kanban_board

// Второй вызов в той же транзакции → из кэша Hibernate (Identity Map)
KanbanBoard board2 = boardRepository.findById(1L).get(); // NO SQL, из 1-го уровня кэша

assert board1 == board2; // true: тот же объект в памяти
```

**Hibernate First-Level Cache** (Session Cache) = реализация Identity Map:
- Область видимости: одна транзакция / один HTTP-запрос (через `@Transactional`)
- Автоматически активен без конфигурации
- Гарантирует уникальность объектов по (EntityClass, id)

### Явная демонстрация в сервисе

```java
@Transactional
public ProjectResponseDTO addWorkerToProject(Long projectId, Long userId) {
    // Identity Map: если project уже загружен в этой транзакции — повторного SQL нет
    KanbanProject project = projectRepository.findById(projectId)
        .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

    User user = userRepository.findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException("User not found"));

    ProjectMember member = ProjectMember.builder()
        .project(project)  // ссылка на уже кешированный объект
        .user(user)
        .role(ProjectMemberRole.WORKER)
        .build();

    // При сохранении Hibernate снова использует Identity Map для project и user
    memberRepository.save(member);
    return modelMapper.map(project, ProjectResponseDTO.class);
}
```

---

## Lazy Load (Рекомендуемый)

### Описание
**Lazy Load** — откладывает загрузку связанных объектов до момента первого обращения к ним.

### Реализация в проекте

```java
// KanbanProject.java — доски загружаются только когда к ним обращаются
@OneToMany(mappedBy = "project",
           cascade = CascadeType.ALL,
           orphanRemoval = true,
           fetch = FetchType.LAZY)  // <-- Lazy Load
private Set<KanbanBoard> boards = new HashSet<>();

// KanbanBoard.java — задачи загружаются только при явном вызове
@OneToMany(mappedBy = "board",
           cascade = CascadeType.ALL,
           orphanRemoval = true,
           fetch = FetchType.LAZY)  // <-- Lazy Load
private Set<KanbanTask> tasks = new HashSet<>();
```

### Эффект
- При загрузке проекта не выполняется JOIN с досками и задачами
- Снижение нагрузки на БД при запросах списков
- Данные загружаются только если сервис явно к ним обращается

---

## Применение паттернов в архитектуре PCMEF

| Паттерн | Слой | Класс | Обязательность |
|---------|------|-------|---------------|
| Data Mapper | M (Mediator) | Все Service-классы + ModelMapper | Обязательно |
| Identity Map | F (Foundation) | Hibernate Session (1st level cache) | Обязательно |
| Lazy Load | E (Entity) | KanbanProject, KanbanBoard, KanbanTask, KanbanProject | Рекомендуется |
