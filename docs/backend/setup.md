# Backend — Настройка окружения

Требования:

- Java 17 или выше
- Maven (рекомендуется использовать встроенный wrapper `./mvnw`)

Запуск локально:

1. Перейти в каталог `backend`:

   cd backend

2. Сборка и запуск:

   ./mvnw clean package
   java -jar target/*.jar

Переменные окружения и конфигурация:

- Конфигурационные файлы: `src/main/resources/application.yml` и `application.properties`.
- В `target/classes/` также присутствуют собранные конфиги при билде.
