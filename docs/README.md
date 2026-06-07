# Student Tracker — Документация проекта

Официальная документация курсового проекта **Student Tracker** (траектория Enterprise): учёт учебных групп, предметов, материалов и статусов их выполнения.

**Автор:** Мадаев Алихан Ахьядович · **Группа:** ПИЖ-б-о-23-1  
**Репозиторий:** https://github.com/alikhan902/student-tracker

## Компоненты системы

| Компонент | Каталог | Технологии |
|-----------|---------|------------|
| Backend | `backend/` | Java 17, Spring Boot 3.3, PostgreSQL |
| Frontend | `frontend/` | React 19, Vite, Tailwind CSS |
| Desktop | `desktop/` | Java 17, JavaFX 21 |
| Прокси | `nginx/` | Nginx, HTTPS |

## Структура документации

### 00 — Инициация
- [Паспорт проекта](00-initiation/project-passport.md)
- [Бизнес-контекст (IDEF0)](00-initiation/business-context.md)
- [Бизнес-глоссарий](00-initiation/business-glossary.md)
- [Бизнес-модель классов](00-initiation/business-class-model.md)
- [Диаграмма BUC](00-initiation/buc-diagram.md)
- [Матрица стейкхолдеров](00-initiation/stakeholder-matrix.md)
- [SWOT-анализ](00-initiation/swot-analysis.md)

### 01 — Требования
- [Доменная модель](01-requirements/domain-model.md)
- [Расширенный глоссарий](01-requirements/glossary-extended.md)
- [Диаграмма прецедентов](01-requirements/use-case-diagram.md)
- [Спецификация прецедентов](01-requirements/use-case-specification.md)
- [Матрица трассировки](01-requirements/traceability-matrix.md)

### 02 — Архитектура
- [ADR — архитектурные решения](02-architecture/adr.md)
- [Диаграмма зависимостей](02-architecture/dependency-diagram.md)
- [Спецификация интерфейсов PCMEF](02-architecture/interface-specification.md)
- [Пакетная диаграмма PCMEF](02-architecture/pcmef-package-diagram.md)

### 03 — База данных
- [ER-диаграмма](03-database/er-diagram.md)
- [DDL-скрипты](03-database/ddl-scripts.md)
- [Стратегия ORM](03-database/orm-strategy.md)

### 04 — Проектирование
- [Диаграмма классов](04-design/class-diagram.md)
- [Спецификация методов](04-design/method-specification.md)
- [Диаграммы последовательности](04-design/sequence-diagrams.md)

### 05 — Реализация
- [Заметки по реализации](05-implementation/implementation-notes.md)

### 06 — Рефакторинг
- [Паттерны](06-refactoring/patterns.md)
- [Отчёт статического анализа](06-refactoring/static-analysis-report.md)

### 07 — Управление
- [Техническое задание](07-management/technical-specification.md)
- [Пояснительная записка](07-management/explanatory-note.md)
- [Руководство пользователя](07-management/user-manual.md)
- [Руководство администратора](07-management/admin-manual.md)
- [WBS](07-management/wbs.md)
- [Диаграмма Ганта](07-management/gantt.md)
- [Оценка COCOMO](07-management/cocomo.md)

## Диаграммы (PlantUML)

Исходники в [`docs/images/`](images/README.md) — структура как в `docs-templ/images/`. Рендер: `plantuml docs/images/**/*.puml`.

## Быстрый старт

```bash
cp .env.example .env   # заполнить переменные
docker compose up --build -d
```

Подробности развёртывания — в [руководстве администратора](07-management/admin-manual.md) и [заметках по реализации](05-implementation/implementation-notes.md).
