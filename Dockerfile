FROM openjdk:17-jdk-slim

LABEL authors="Angel"

WORKDIR /app

COPY target/your-app.jar app.jar

CMD ["java", "-jar", "app.jar"]