# Стратегия ORM (Spring Data JPA / Hibernate)

## Обзор

В проекте используется **Spring Data JPA** поверх **Hibernate** как провайдера ORM.  
Каждая бизнес-сущность отображается на отдельную таблицу PostgreSQL.

---

## Стратегия наследования

Наследование не используется — все сущности являются независимыми `@Entity`-классами.

---

## Маппинг сущностей

### User → `app_user`

| Java-поле | Колонка БД | JPA-аннотация |
|-----------|-----------|---------------|
| `id` | `id` | `@Id @GeneratedValue(IDENTITY)` |
| `name` | `name` | `@Column(nullable = false)` |
| `username` | `username` | `@Column(unique = true, nullable = false)` |
| `email` | `email` | `@Column(unique = true)` |
| `password` | `password` | `@Column(nullable = false)` |
| `roles` | `user_roles` (отдельная таблица) | `@ElementCollection(EAGER)` |
| `company` | `company_id` | `@ManyToOne(EAGER) @JoinColumn` |

### KanbanProject → `kanban_project`

| Java-поле | Колонка | JPA-аннотация |
|-----------|---------|---------------|
| `id` | `id` | `@Id @GeneratedValue(IDENTITY)` |
| `name` | `name` | `@Column(nullable = false)` |
| `company` | `company_id` | `@ManyToOne(LAZY) @JoinColumn(nullable = false)` |
| `members` | → `project_member` | `@OneToMany(mappedBy = "project", CASCADE.ALL, orphanRemoval)` |
| `boards` | → `kanban_board` | `@OneToMany(mappedBy = "project", CASCADE.ALL, orphanRemoval)` |

### KanbanTask → `kanban_task`

| Java-поле | Колонка | JPA-аннотация |
|-----------|---------|---------------|
| `status` | `status` | `@Enumerated(EnumType.STRING)` |
| `board` | `board_id` | `@ManyToOne(LAZY) @JoinColumn(nullable = false)` |
| `createdAt` | `created_at` | `@PrePersist` lifecycle callback |
| `updatedAt` | `updated_at` | `@PreUpdate` lifecycle callback |

---

## Стратегия загрузки (Fetch)

| Связь | Стратегия | Обоснование |
|-------|-----------|------------|
| `User.company` | `EAGER` | Компания нужна при каждом запросе авторизации |
| `User.roles` | `EAGER` | Роли нужны Spring Security немедленно |
| `KanbanProject.boards` | `LAZY` | Доски загружаются только при явном запросе |
| `KanbanProject.members` | `LAZY` | Участники загружаются только при явном запросе |
| `KanbanBoard.tasks` | `LAZY` | Задачи загружаются только при явном запросе |

---

## Каскадирование

| Родитель | Дочерняя связь | Каскад | Orphan Removal |
|----------|---------------|--------|---------------|
| `KanbanProject` | boards | ALL | true |
| `KanbanProject` | members | ALL | true |
| `KanbanBoard` | tasks | ALL | true |

При удалении проекта → каскадно удаляются все его доски и участники.  
При удалении доски → каскадно удаляются все её задачи.

---

## Lifecycle Callbacks

```java
// Автоматическая установка createdAt при сохранении
@PrePersist
public void prePersist() {
    if (createdAt == null) {
        createdAt = LocalDateTime.now();
    }
}

// Автоматическое обновление updatedAt при изменении (KanbanTask)
@PreUpdate
public void preUpdate() {
    updatedAt = LocalDateTime.now();
}
```

---

## Настройки Hibernate (application.properties)

```properties
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.properties.hibernate.format_sql=true
```
