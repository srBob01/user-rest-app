FROM openjdk:21-jdk-slim-buster
LABEL authors="Nikita"

WORKDIR /app

COPY . .

RUN ./gradlew clean build

EXPOSE 8080


CMD ["java", "-jar", "./build/libs/app-0.0.1-SNAPSHOT.jar"]