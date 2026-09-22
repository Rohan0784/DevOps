FROM eclipse-temurin:11-jre

WORKDIR /app

# Copy the self-contained JAR built by Maven.
COPY target/se_methods-1.0-SNAPSHOT-jar-with-dependencies.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
