# Stage 1: Build the application
FROM maven:3.9.6-eclipse-temurin-17 AS build
COPY . /hoidanit/jobhunter
WORKDIR /hoidanit/jobhunter

# Skip tests to speed up the build
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM openjdk:17-slim
EXPOSE 8080
COPY --from=build /hoidanit/jobhunter/target/*.jar /hoidanit/spring-boot-job-hunter.jar
ENTRYPOINT ["java", "-jar", "/hoidanit/spring-boot-job-hunter.jar"]
