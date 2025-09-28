FROM eclipse-temurin:17-jdk
WORKDIR /app

COPY target/*.jar app.jar
COPY src/main/resources/configurations /app/classes/configurations

ENTRYPOINT ["java", "-jar", "app.jar"]
