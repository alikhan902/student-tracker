# Диаграмма Ганта (план-факт)

**Проект:** Student Tracker · **Срок:** 01.01.2026 — 05.06.2026

```mermaid
gantt
  title Student Tracker — план работ
  dateFormat YYYY-MM-DD
  section Инициация
  Паспорт и требования     :a1, 2026-01-01, 14d
  section Архитектура
  ADR и PCMEF              :a2, after a1, 10d
  section Backend
  Auth и Security          :b1, 2026-01-20, 14d
  Groups и Subjects        :b2, after b1, 21d
  Materials и Complete     :b3, after b2, 14d
  section Frontend
  UI страницы              :f1, 2026-02-15, 28d
  section Desktop
  JavaFX клиент            :d1, 2026-03-15, 28d
  section Инфра
  Docker и Nginx           :i1, 2026-04-01, 14d
  section QA
  Тесты и рефакторинг      :q1, 2026-04-20, 21d
  section Документация
  Комплект docs            :doc1, 2026-05-10, 21d
  Сдача                    :milestone, 2026-06-05, 1d
```

## Вехи

| Дата | Веха |
|------|------|
| 20.01.2026 | Рабочий login + JWT |
| 15.03.2026 | CRUD предметов и материалов |
| 01.04.2026 | Docker Compose |
| 15.05.2026 | Desktop feature-complete |
| 05.06.2026 | Сдача проекта |
