FROM ubuntu:latest AS build

# Install necessary dependencies
RUN apt-get update && apt-get install -y \
    openjdk-17-jdk \
    maven

# Set working directory
WORKDIR /app

# Copy all files to the container
COPY . .

# Build the Maven project (this will create the JAR file)
RUN mvn clean package

# Second stage: build a lightweight image for running the JAR
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy the JAR file from the previous stage
COPY --from=build /app/target/your-artifact-name.jar app.jar

# Expose port 8080
EXPOSE 8080

# Command to run the application
CMD ["java", "-jar", "app.jar"]
