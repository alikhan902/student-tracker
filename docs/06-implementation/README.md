# Отчёты этапа реализации

| Отчёт | Файл |
|-------|------|
| Покрытие тестами (сводка) | [test-coverage-report.md](test-coverage-report.md) |
| JaCoCo HTML (backend) | [jacoco/index.html](jacoco/index.html) |
| Статический анализ (Checkstyle, ESLint) | [static-analysis-report.md](static-analysis-report.md) |

## Обновление отчётов

```bash
# Backend: JaCoCo + Checkstyle
cd backend
./mvnw clean test checkstyle:check
cp -r target/site/jacoco/* ../docs/06-implementation/jacoco/

# Frontend: Vitest coverage + ESLint
cd frontend
npm run coverage
npm run lint
```

Windows (PowerShell):

```powershell
cd backend
.\mvnw.cmd clean test checkstyle:check
Copy-Item -Recurse -Force target\site\jacoco\* ..\docs\06-implementation\jacoco\
```
