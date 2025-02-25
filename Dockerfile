FROM --platform=linux/arm64 maven:3.9.9-eclipse-temurin-17 AS builder

COPY ./pom.xml pom.xml
COPY ./src ./src/

RUN mvn clean verify

FROM --platform=linux/arm64 eclipse-temurin:17-jre

COPY --from=builder target/*.jar task-management.jar

EXPOSE 8080

CMD ["java", "-jar", "task-management.jar"]