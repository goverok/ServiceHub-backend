# === Stage 1: Build ===
FROM gradle:8.10.2-jdk21 AS builder
WORKDIR /app

# копируем wrapper и настройки (для кэширования зависимостей)
COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts settings.gradle.kts gradle.properties ./

# прогреем зависимости
RUN ./gradlew --no-daemon dependencies || return 0

# копируем исходники
COPY . .

# собираем bootJar только для app
RUN ./gradlew :app:bootJar --no-daemon -x test

# === Stage 2: Runtime ===
FROM eclipse-temurin:24-jdk

WORKDIR /app

COPY --from=builder /app/app/build/libs/app-*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]