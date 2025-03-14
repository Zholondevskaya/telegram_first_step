# Используем официальный образ с JDK
FROM openjdk:17-jdk-slim

# Устанавливаем рабочую директорию внутри контейнера
WORKDIR /app

# Копируем собранный JAR-файл и конфигурацию
COPY target/Telegram*.jar app.jar
COPY src/main/resources/application.properties application.properties

# Указываем команду для запуска приложения
CMD ["java", "-jar", "app.jar"]
