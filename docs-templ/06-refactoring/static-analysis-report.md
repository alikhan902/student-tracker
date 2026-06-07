# Отчёт статического анализа

## Инструменты

| Инструмент | Назначение | Статус |
|-----------|-----------|--------|
| Checkstyle | Стиль кода (Google Java Style) | Не запущен (нет конфигурации в pom.xml) |
| SonarQube / SonarLint | Нахождение дефектов кода и уязвимостей | Частично (через SonarLint в IDE) |
| SpotBugs | Поиск потенциальных ошибок | Не запущен |

---

## Ручной анализ кода (Code Review)

### Выявленные проблемы

| Код | Серьёзность | Файл | Описание | Статус |
|-----|------------|------|----------|--------|
| SA-001 | ⚠️ Средняя | `UserController.java:30` | Метод `getCurrentUser()` дублирует логику, которая должна быть в Security-слое | Выявлено |
| SA-002 | ⚠️ Средняя | `docker-compose.yml:9` | Пароль PostgreSQL захардкожен в `docker-compose.yml` (pilot7736) | Выявлено |
| SA-003 | ⚠️ Средняя | `AuthController.java:28` | Метод `signup` назван `login` — опечатка в имени | Выявлено |
| SA-004 | ℹ️ Низкая | `entity/User.java:16` | Неиспользуемый импорт `java.net.ProtocolFamily` | Выявлено |
| SA-005 | ℹ️ Низкая | `TaskStatus.java` | Только два статуса (TODO/DONE) — ограниченная модель Kanban | Известное ограничение |
| SA-006 | ✅ Норма | `entity/` | Все сущности используют `@Builder`, `@Getter`, `@Setter` через Lombok | OK |
| SA-007 | ✅ Норма | `service/` | `@RequiredArgsConstructor` для DI через конструктор | OK |

---

## Команды для запуска анализа

### Checkstyle (добавить в pom.xml)

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-checkstyle-plugin</artifactId>
    <version>3.3.1</version>
    <configuration>
        <configLocation>google_checks.xml</configLocation>
        <failsOnError>true</failsOnError>
    </configuration>
</plugin>
```

```bash
./mvnw checkstyle:check
```

### SonarQube (локально через Docker)

```bash
docker run -d --name sonar -p 9000:9000 sonarqube:lts-community
./mvnw sonar:sonar \
  -Dsonar.projectKey=kanbaNice \
  -Dsonar.host.url=http://localhost:9000 \
  -Dsonar.token=<your-token>
```

### SpotBugs

```bash
./mvnw spotbugs:check
```

---

## Метрики качества кода (оценочные)

| Метрика | Значение | Порог |
|---------|---------|-------|
| Дублирование кода | ~5% | < 10% |
| Средняя длина метода | ~15 строк | < 30 |
| Цикломатическая сложность | ~3 | < 10 |
| Покрытие тестами | 0% | **> 40% (не выполнено)** |
| Количество TODO в коде | 0 | 0 |
| Unused imports | 1 (User.java) | 0 |
