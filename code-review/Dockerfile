FROM openjdk:11 AS builder
WORKDIR application
COPY . .
RUN chmod +x ./gradlew
RUN ./gradlew bootJAR

FROM openjdk:11
WORKDIR application
COPY --from=builder build/libs/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/app.jar"]
