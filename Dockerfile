# Используем официальный образ с JDK
FROM openjdk:17-jdk-slim

# Устанавливаем рабочую директорию внутри контейнера
WORKDIR /app

# Копируем собранный JAR-файл и конфигурацию
COPY target/Telegram*.jar app.jar
COPY target/dependency/ /app/libs/
COPY src/main/resources/application-docker.properties application-docker.properties

# Указываем команду для запуска приложения
CMD ["java", "-classpath", "app.jar:libs/*", "org.example.Main"]
