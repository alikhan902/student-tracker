# Паттерны рефакторинга

## Data Mapper (обязательный)

Паттерн разделяет JPA-сущности и DTO. В Student Tracker реализован через **ModelMapper** в сервисах.

```java
@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements IGroupService {
    private final GroupRepository groupRepository;
    private final ModelMapper modelMapper;

    public GroupResponseDTO getMyGroup() {
        Group group = /* load */;
        return modelMapper.map(group, GroupResponseDTO.class);
    }
}
```

**Выгода:** API-контракты не протекают в Entity; клиенты не зависят от LAZY-полей Hibernate.

---

## Repository (Foundation)

Spring Data JPA скрывает SQL:

```java
public interface TrainingSubjectRepository extends JpaRepository<TrainingSubject, Long> {
    List<TrainingSubject> findByGroupId(Long groupId);
}
```

---

## Strategy (валидация доступа)

`ValidationServiceImpl` инкапсулирует проверку «только староста» / принадлежность к группе — переиспользуется в `GroupServiceImpl` и subject/material сервисах.

---

## Front Controller

`GlobalExceptionHandler` — единая точка преобразования исключений в HTTP-ответы (`ResourceNotFoundException` → 404, JWT errors → 401).

---

## Рекомендации по дальнейшему рефакторингу

1. Разбить `desktop/.../App.java` на отдельные view/controller классы (как заготовки в `fxml/`).
2. Удалить неиспользуемую сущность `Role` или подключить к модели.
3. Вынести permission-строки из шаблона «expense app» в доменно-релевантные.
4. Добавить интеграционные тесты `@SpringBootTest` для контроллеров.
