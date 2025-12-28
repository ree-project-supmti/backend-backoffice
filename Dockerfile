# Multi-stage build for Spring Boot application
FROM maven:3.9-eclipse-temurin-21-alpine AS build

WORKDIR /app

# Copy pom.xml and download dependencies (cached layer)
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .
RUN mvn dependency:go-offline -B

# Copy source code and build
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Create non-root user for security
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copy the built jar from build stage
COPY --from=build /app/target/si-releves-0.0.1-SNAPSHOT.jar app.jar

# Expose port (Render will set PORT env variable)
EXPOSE 8080

# Use environment variable for port configuration
ENTRYPOINT ["java", "-jar", "app.jar"]
