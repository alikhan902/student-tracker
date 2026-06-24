# Отчёт о покрытии тестами

**Проект:** Student Tracker  
**Дата:** 05.06.2026  
**Инструменты:** JaCoCo 0.8.11 (Java), Vitest + v8 (JavaScript)

---

## 1. Сводка

| Модуль | Инструмент | Test-классов / файлов | Покрытие строк | Требование НФТ-05 |
|--------|------------|----------------------|----------------|-------------------|
| Backend | JaCoCo | 21 | ~48% | > 40% ✅ |
| Frontend | Vitest v8 | 8 | ~91% (UI-компоненты) | > 40% ✅ |
| Desktop | JaCoCo | 3 | ~35% | зона роста |
| **Сводно (backend + frontend)** | — | 29 | **> 40%** | **✅ выполнено** |

---

## 2. HTML-отчёты

| Модуль | Путь к HTML-отчёту |
|--------|-------------------|
| Backend (JaCoCo) | [jacoco/index.html](jacoco/index.html) |
| Frontend (Vitest) | `frontend/coverage/lcov-report/index.html` |

---

## 3. Backend — JaCoCo

### 3.1. Покрытые пакеты

| Пакет | Instruction Cov. | Line Cov. | Примечание |
|-------|-----------------|-----------|------------|
| `entity` | ~72% | ~75% | Unit-тесты всех сущностей и enum |
| `entity.type` | ~95% | ~95% | RoleType, StudentType, PermissionType |
| `Exception` | ~88% | ~88% | Custom exceptions |
| `Security` | ~60% | ~58% | RolePermissionMapping |
| `service` | ~42% | ~45% | MaterialCompleteService |
| `controller` | ~25% | ~28% | MaterialCompleteController (MockMvc) |

### 3.2. Команда генерации

```bash
cd backend
./mvnw clean test
# отчёт: target/site/jacoco/index.html
# копирование в docs:
./mvnw jacoco:report -Djacoco.outputDirectory=../docs/06-implementation/jacoco
```

---

## 4. Frontend — Vitest

Покрытие по отчёту `frontend/coverage/lcov-report/`:

| Метрика | Значение |
|---------|----------|
| Statements | 90.95% (382/420) |
| Branches | 71.15% (37/52) |
| Functions | 76.19% (16/21) |
| Lines | 90.95% (382/420) |

Протестированные компоненты: `StatCard`, `Modal`, `Layout`, `Sidebar`, `SubjectCard`, `SubjectSection`, `GroupStudentCard`, `PrivateRoute`.

### Команда генерации

```bash
cd frontend
npm run coverage
```

---

## 5. Desktop — JaCoCo

| Test-класс | Область |
|------------|---------|
| `UserProfileTest` | Модель профиля |
| `SubjectTest` | Модель предмета |
| `BackendApiReflectionTest` | Рефлексия API-клиента |

```bash
cd desktop
./mvnw clean test
# отчёт: target/site/jacoco/index.html
```

---

## 6. Вывод

Требование **НФТ-05** (покрытие тестами > 40%) выполнено за счёт backend unit-тестов (entity, service, security) и frontend Vitest-тестов UI-компонентов. Рекомендуется расширить интеграционные тесты controller-слоя и покрытие страниц React.
