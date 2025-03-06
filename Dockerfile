FROM gradle:8.5-jdk21 AS builder

WORKDIR /app

COPY . .
RUN chmod +x ./gradlew
RUN ./gradlew clean build -x test bootJar --no-daemon

RUN rm -rf /root/.gradle


FROM openjdk:21-jdk

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar /app/app.jar

EXPOSE 8080

CMD ["java", "-jar", "/app/app.jar"]
