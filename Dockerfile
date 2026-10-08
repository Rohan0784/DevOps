FROM amazoncorretto:17
COPY ./target/seMethod-0.1.0.4-jar-with-dependencies.jar /tmp/
WORKDIR /tmp
ENTRYPOINT ["java", "-jar", "seMethod-0.1.0.4-jar-with-dependencies.jar"]
