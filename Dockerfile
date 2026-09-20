FROM eclipse-temurin:11-jre

WORKDIR /app

# Copies the JAR built by Maven into the image
COPY target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]