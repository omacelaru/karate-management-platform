# Stage 1: Build the application with Maven
FROM maven:3.9.9-eclipse-temurin-23 AS builder

# Set the working directory
WORKDIR /app

# Copy the necessary files to build the application (pom.xml and source files)
COPY pom.xml .
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Stage 2: Build the production image
FROM openjdk:23-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the .jar file from the build stage
COPY --from=builder /app/target/karate-management-platform*.jar /app/karate-management-platform.jar

# Expose the port that the application will listen on
EXPOSE 8080

# Command to run the Spring Boot application
ENTRYPOINT ["java", "-jar", "/app/karate-management-platform.jar"]
