FROM maven:3.9.9-eclipse-temurin-17 AS dev

WORKDIR /app

# Copy pom and maven wrapper first for better layer caching
COPY pom.xml /app/pom.xml
COPY mvnw /app/mvnw
COPY .mvn /app/.mvn

# Download dependencies (without -q to show progress)
RUN mvn -DskipTests dependency:resolve dependency:resolve-plugins && \
    chmod +x ./mvnw

# Copy source code
COPY . /app

# Expose backend port
EXPOSE 8080

CMD ["./mvnw", "spring-boot:run", "-Dspring-boot.run.host=0.0.0.0"]

