# Desktop — Тестирование

Unit и интеграционные тесты запускаются через Maven:

   ./mvnw test

Результаты тестов и отчёты находятся в `desktop/target/surefire-reports/` и `target/jacoco.exec`.

UI-тесты (если есть) — проверить наличие зависимостей и конфигураций в `pom.xml`.
