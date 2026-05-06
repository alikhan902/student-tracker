FROM maven:3.9.9-eclipse-temurin-17 AS dev

WORKDIR /app

# Copy only pom first for better caching
COPY pom.xml /app/pom.xml
RUN mvn -q -DskipTests dependency:go-offline

# Copy source
COPY . /app

# Expose backend port
EXPOSE 8080

# Ensure wrapper is executable
RUN chmod +x ./mvnw || true

CMD ["./mvnw", "spring-boot:run", "-Dspring-boot.run.host=0.0.0.0"]

