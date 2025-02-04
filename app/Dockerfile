# Используем минимальный образ OpenJDK 21
FROM openjdk:21-jdk-slim-buster AS build

LABEL authors="Nikita"

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем только файлы Gradle (ускоряет кэширование)
COPY gradlew gradlew
COPY gradle gradle
COPY build.gradle build.gradle
COPY settings.gradle settings.gradle

# Даем права на выполнение Gradle Wrapper
RUN chmod +x gradlew

# Загружаем зависимости без сборки (кэшируем их)
RUN ./gradlew dependencies --no-daemon || return 0

# Копируем исходный код
COPY . .

# Собираем приложение без тестов
RUN ./gradlew clean build -x test --no-daemon

# Второй минимизированный этап для уменьшения размера образа
FROM openjdk:21-jdk-slim-buster

WORKDIR /app

# Копируем только JAR-файл из предыдущего этапа
COPY --from=build /app/build/libs/app-0.0.1-SNAPSHOT.jar app.jar

# Открываем порт
EXPOSE 8080

# Устанавливаем запуск JAR с оптимизированными параметрами
CMD ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-XX:+UseG1GC", "-jar", "/app/app.jar"]