FROM openjdk:21-jdk-slim

RUN ./gradlew clean build -x test

COPY ./build/libs/*-SNAPSHOT.jar /app.jar

EXPOSE 80

ENTRYPOINT ["java", "-jar", "app.jar"]
