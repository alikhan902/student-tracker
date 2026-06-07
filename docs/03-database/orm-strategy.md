# Стратегия ORM (JPA / Hibernate)

## Общие принципы

- **Провайдер:** Hibernate через Spring Data JPA
- **Сущности:** `jakarta.persistence` в пакете `entity/`
- **Репозитории:** интерфейсы `JpaRepository` в `Repository/`
- **Транзакции:** `@Transactional` на сервисном слое

## Маппинг ключевых сущностей

| Сущность | Таблица | Особенности |
|----------|---------|-------------|
| `User` | `app_user` | `UserDetails`, `@ElementCollection` для ролей |
| `Group` | `app_group` | `cascade ALL` на subjects |
| `TrainingSubject` | `training_subject` | `ManyToOne` Group |
| `EducationalMaterial` | `educational_materials` | `ManyToOne` subject |
| `MaterialComplete` | `material_complete` | `@EmbeddedId`, `@MapsId` |

## Каскады и orphanRemoval

```java
// Group.java — удаление группы удаляет предметы
@OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
private Set<TrainingSubject> trainingSubjects;

// TrainingSubject — удаление предмета удаляет материалы
@OneToMany(mappedBy = "trainingSubject", cascade = CascadeType.ALL, orphanRemoval = true)
private Set<EducationalMaterial> materials;
```

## Fetch-стратегия

- `User.group` — `EAGER` (частые проверки принадлежности к группе)
- Коллекции группы — `LAZY` (загрузка по необходимости в сервисах)

## DTO и Entity

Слой API не возвращает сущности напрямую: **ModelMapper** преобразует Entity → DTO в сервисах (паттерн Data Mapper).

## Миграции

Flyway/Liquibase в проекте не подключены; для учебного стенда допустим `spring.jpa.hibernate.ddl-auto=update` (см. конфигурацию в `application.yml` / переменные окружения).
