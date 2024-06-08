# maven image for build
FROM maven:3.8.4-openjdk-17 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

# Build application
RUN mvn clean package # -DskipTests


# openjdk for run
FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=build /app/target/MoviesLibrary-0.1.jar ./MoviesLibrary.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "MoviesLibrary.jar"]

