# Диаграммы PlantUML — Student Tracker

Структура повторяет `docs-templ/images/`. Исходники — `.puml`; для отчёта можно экспортировать в PNG.

## Рендер

```bash
# Установка (пример)
# sudo apt install plantuml   или  java -jar plantuml.jar

plantuml docs/images/00/*.puml
plantuml docs/images/01/*.puml
plantuml docs/images/02/*.puml
plantuml docs/images/03/*.puml
plantuml docs/images/04/*.puml
```

Онлайн: [plantuml.com](https://www.plantuml.com/plantuml/uml/) — вставить содержимое `.puml`.

## Каталог

| Файл | Назначение |
|------|------------|
| `00/business_IDEF0.puml` | IDEF0 A-0 |
| `00/business_classes.puml` | Бизнес-модель классов |
| `00/business_precedents.puml` | BUC (бизнес-прецеденты) |
| `01/domain_model.puml` | Доменная модель |
| `01/use_case.puml` | Системные прецеденты |
| `02/PCMEF.puml` | Архитектура PCMEF |
| `02/dependency_diagram.puml` | Зависимости компонентов |
| `03/ER.puml` | ER (PostgreSQL) |
| `04/class_diagram.puml` | Классы backend |
| `04/sequence_diagram_1.puml` | Вход POST /auth/login |
| `04/sequence_diagram_2.puml` | Создание предмета |
| `04/sequence_diagram_3.puml` | Добавление студента в группу |
| `04/sequence_diagram_4.puml` | OAuth2 Google |

## Git-статистика

| Файл | Описание |
|------|----------|
| `git-activity-graph.png` / `.svg` | График активности коммитов |
| `git-contribution-heatmap.png` / `.svg` | Тепловая карта вкладов (contribution graph) |

Снимки соответствуют репозиторию https://github.com/alikhan902/student-tracker (Insights → Pulse).
