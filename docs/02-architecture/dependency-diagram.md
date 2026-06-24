# Диаграмма зависимостей компонентов

## Диаграмма

**PlantUML:** [`images/02/dependency_diagram.puml`](../images/02/dependency_diagram.puml)

## Зависимости между модулями репозитория

| Модуль | Зависит от |
|--------|------------|
| `frontend` | backend API (через proxy / nginx) |
| `desktop` | backend API (`http://localhost:8080`) |
| `backend` | PostgreSQL, SMTP (опционально), файловая система |
| `nginx` | frontend:5173, backend:8080 |
