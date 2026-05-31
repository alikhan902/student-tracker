# Backend — Сборка и деплой

Docker:

1. В корне `backend/` есть `Dockerfile`.
2. Собрать образ:

   docker build -t student-tracker-backend:latest .

3. Запустить контейнер:

   docker run -e SPRING_PROFILES_ACTIVE=prod -p 8080:8080 student-tracker-backend:latest

CI:

- В CI рекомендуется выполнять `./mvnw -DskipTests package` и затем собирать Docker-образ.
