# Build stage - Install Maven for building the application
FROM maven:3.8.4-openjdk-11 AS build

# Set working directory
WORKDIR /app

# Copy pom.xml and download dependencies (for better layer caching)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Production stage
FROM openjdk:11-jre-slim

# Create app directory
WORKDIR /app

# Copy the built fat JAR from build stage
COPY --from=build /app/target/*-jar-with-dependencies.jar app.jar

# Create non-root user for security
RUN groupadd -r appuser && useradd -r -g appuser appuser
RUN chown -R appuser:appuser /app
USER appuser

# Expose port (if your app has a web interface later)
EXPOSE 8081

# Set the entrypoint
ENTRYPOINT ["java", "-jar", "app.jar"]
