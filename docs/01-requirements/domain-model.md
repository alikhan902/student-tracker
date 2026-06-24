# Domain Model (концептуальная модель)

## Диаграмма

**PlantUML (domain model):** [`images/01/domain_model.puml`](../images/01/domain_model.puml)

## Концептуальные связи

| Связь | Тип | Мощность | Описание |
|-------|-----|----------|----------|
| Group → User | Агрегация | 1 : 0..* | Группа объединяет пользователей |
| Group → TrainingSubject | Композиция | 1 : 0..* | Предметы принадлежат группе |
| TrainingSubject → EducationalMaterial | Композиция | 1 : 0..* | Материалы принадлежат предмету |
| User + EducationalMaterial → MaterialComplete | Ассоциация | составной ключ | Статус выполнения для пары пользователь–материал |
