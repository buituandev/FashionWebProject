# First stage: build the application
FROM ubuntu:latest AS build 

# Install necessary dependencies
RUN apt-get update && apt-get install -y \
    openjdk-17-jdk \
    maven

# Set working directory
WORKDIR /app

# Copy all files to the container
COPY . .

# Move the JSON config file to src/main/resources
RUN mkdir -p src/main/resources && \
    mv firebase-config.json src/main/resources/

# Build the Maven project (this will create the JAR file)
RUN mvn clean package

# Second stage: build a lightweight image for running the JAR
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy the JAR file from the previous stage
COPY --from=build /app/target/Fashion-1.jar app.jar

# Copy the firebase config file to the correct location in the final image
COPY --from=build /app/src/main/resources/firebase-config.json src/main/resources/firebase-config.json

# Expose port 8080
EXPOSE 8080

# Command to run the application
CMD ["java", "-jar", "app.jar"]
