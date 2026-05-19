FROM maven:3.9.4-eclipse-temurin-21-alpine AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn -B dependency:go-offline
COPY src ./src
COPY *.json ./
RUN mvn -B package -DskipTests

FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
COPY --from=builder /app/*.json ./
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
