FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY target/message-uuid-generator-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar", "--spring.profiles.active=docker"]