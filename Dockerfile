FROM openjdk:17
COPY target/se-methods.jar /tmp
WORKDIR /tmp
ENTRYPOINT ["java", "-jar", "se-methods.jar"]