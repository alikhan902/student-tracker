# Пакетная диаграмма PCMEF

## Диаграмма

**PlantUML:** [`images/02/PCMEF.puml`](../images/02/PCMEF.puml)

## Соответствие каталогов

| Слой PCMEF | Путь в репозитории |
|------------|-------------------|
| P (web) | `frontend/src/` |
| P (desktop) | `desktop/src/main/java/student_tracker/` |
| C | `backend/.../controller/` |
| M | `backend/.../service/`, `Security/` |
| E | `backend/.../entity/` |
| F | `backend/.../Repository/` |

## Правила зависимостей

- `controller` → `service`, `dto` (не → `entity` напрямую, кроме исключений)
- `service` → `Repository`, `entity`, `dto`
- `Repository` → `entity` только
- `frontend` / `desktop` не импортируют Java-классы backend
