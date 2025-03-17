# Используем официальный образ с JDK
FROM openjdk:17-jdk-slim

# Устанавливаем рабочую директорию внутри контейнера
WORKDIR /app

# Копируем собранный JAR-файл и конфигурацию
COPY target/Telegram*.jar app.jar
COPY target/dependency/ libs/
COPY src/main/resources/application-docker.properties application.properties

# Указываем команду для запуска приложения
CMD ["java", "-Dtelegram.config.location=/app/application.properties", "-classpath", "app.jar:libs/*", "org.example.Application"]
