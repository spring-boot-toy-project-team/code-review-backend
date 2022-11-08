FROM openjdk:11-jre-slim AS builder
WORKDIR application
COPY . .
RUN chmod +x ./gradlew
RUN ./gradlew bootJAR

FROM openjdk:11-jre-slim
WORKDIR application
COPY --from=builder build/libs/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/app.jar"]
