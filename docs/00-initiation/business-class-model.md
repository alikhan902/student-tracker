# Бизнес-модель классов (концептуальный уровень)

## Диаграмма

**PlantUML:** [`images/00/business_classes.puml`](../images/00/business_classes.puml)

## Описание связей

| Связь | Тип | Мощность | Описание |
|-------|-----|----------|----------|
| Group → User | Агрегация | 1 : 0..* | Группа объединяет студентов |
| Group → TrainingSubject | Композиция | 1 : 0..* | Предметы принадлежат группе |
| TrainingSubject → EducationalMaterial | Композиция | 1 : 0..* | Материалы привязаны к предмету |
| User ↔ EducationalMaterial | Ассоциация | * : * | Через `MaterialComplete` (статус) |

## Перечисления предметной области

- **StudentType:** `STANDARD`, `HEADMAN`
- **CompletionStatus:** `COMPLETED`, `NOT_COMPLETED`
- **AuthProviderType:** `LOCAL`, `GOOGLE`, `GITHUB`, `EMAIL`
