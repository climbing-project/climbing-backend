FROM openjdk:21-jdk-slim AS builder

COPY . .

RUN ./gradlew clean build -x test

FROM openjdk:21-jdk-slim

RUN mkdir /opt/app

COPY --from=builder build/libs/*-SNAPSHOT.jar opt/app/app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
