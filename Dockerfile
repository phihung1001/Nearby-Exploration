FROM maven:3.9.6-eclipse-temurin-21 AS build

LABEL maintainer="yourname@domain.com"
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src

RUN mvn clean package -DskipTests

FROM openjdk:21-jdk-slim

COPY --from=build /app/target/food-tour-backend-0.0.1-SNAPSHOT.jar /app/food-tour-backend.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/food-tour-backend.jar"]
