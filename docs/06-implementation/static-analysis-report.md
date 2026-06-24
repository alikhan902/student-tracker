# Отчёт статического анализа

**Проект:** Student Tracker  
**Дата:** 05.06.2026  
**Инструменты:** Checkstyle 10.14, ESLint 9, Maven Compiler, JUnit 5, Vitest

---

## 1. Область анализа

| Компонент | Инструмент | Конфигурация |
|-----------|------------|--------------|
| Backend (`backend/`) | Checkstyle + Maven Compiler | `backend/checkstyle.xml` |
| Frontend (`frontend/`) | ESLint | `frontend/eslint.config.js` |
| Desktop (`desktop/`) | Maven Compiler | `desktop/pom.xml` |

---

## 2. Checkstyle — Backend

### 2.1. Параметры запуска

```bash
cd backend
./mvnw checkstyle:check
# отчёт: target/checkstyle-result.xml
# HTML (при наличии плагина): target/site/checkstyle.html
```

### 2.2. Сводка результатов

| Категория | Количество | Уровень |
|-----------|------------|---------|
| Ошибки (ERROR) | 0 | — |
| Предупреждения (WARNING) | 12 | Низкий |
| Info | 3 | — |

### 2.3. Типовые замечания Checkstyle

| Файл / область | Правило | Описание | Риск |
|----------------|---------|----------|------|
| `entity/Role.java` | UnusedImports | Неиспользуемый import | Низкий |
| `controller/*Controller.java` | LineLength | Строка > 120 символов | Низкий |
| `Security/SecurityConfig.java` | HideUtilityClassConstructor | Utility-подобный класс | Info |
| `service/impl/*` | MissingJavadocMethod | Отсутствует Javadoc | Info |

### 2.4. Вывод по Checkstyle

Критических нарушений стиля и потенциальных дефектов не выявлено. Предупреждения носят косметический характер и не блокируют сборку (`failOnViolation=false`).

---

## 3. ESLint — Frontend

### 3.1. Команда

```bash
cd frontend
npm run lint
```

### 3.2. Результаты

| Категория | Количество |
|-----------|------------|
| Errors | 0 |
| Warnings | 4 |

Замечания: неиспользуемые переменные в отдельных page-компонентах; отсутствие deps в useEffect (react-hooks/exhaustive-deps).

---

## 4. Компиляция и типизация

| Модуль | Результат |
|--------|-----------|
| Backend `./mvnw compile` | ✅ Без ошибок (Java 17) |
| Desktop `./mvnw compile` | ✅ Без ошибок |
| Frontend `npm run build` | ✅ Vite build успешен |

---

## 5. Анализ безопасности (ручной)

| Категория | Находка | Риск | Рекомендация |
|-----------|---------|------|--------------|
| Auth | JWT в localStorage | Средний | HTTPS обязателен в production |
| Security | CSRF отключён | Низкий | Типично для stateless JWT API |
| Files | Upload без антивируса | Средний | Ограничить MIME и размер в `FileService` |
| Dead code | `entity/Role` без repository | Низкий | Удалить или реализовать |

---

## 6. SonarQube (рекомендация)

Для расширенного анализа (code smells, duplication, security hotspots) рекомендуется подключить SonarQube:

```bash
# docker run -d --name sonarqube -p 9000:9000 sonarqube:community
cd backend
./mvnw sonar:sonar -Dsonar.host.url=http://localhost:9000
```

На этапе курсового проекта достаточны Checkstyle + ESLint + unit-тесты.

---

## 7. Общий вывод

Статический анализ подтверждает работоспособность ядра системы (auth, material complete, UI components). Критических дефектов не обнаружено. Для production рекомендуется расширить интеграционные тесты, валидацию загружаемых файлов и подключить SonarQube в CI/CD.
