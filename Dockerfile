FROM openjdk:21-jdk-slim

COPY . .

RUN ./gradlew clean build -x test

RUN mkdir /opt/app

COPY ./build/libs/*-SNAPSHOT.jar opt/app/app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
