# -------------------------------
# Step 1: Build the JAR using Maven
# -------------------------------
FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /app

# Copy pom.xml and download dependencies first (layer caching)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and build the app
COPY src ./src
RUN mvn clean package -DskipTests

# -------------------------------
# Step 2: Run the JAR
# -------------------------------
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# Copy the JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose port 8080 (Spring Boot default)
EXPOSE 8080

# Start the app
ENTRYPOINT ["java", "-jar", "app.jar"]
