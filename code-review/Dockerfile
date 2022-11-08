#FROM openjdk:11 AS builder
#WORKDIR application
#COPY . .
#RUN chmod +x ./gradlew
#RUN ./gradlew bootJAR
#
#FROM openjdk:11
#WORKDIR application
#COPY --from=builder build/libs/*.jar app.jar
#EXPOSE 8081
#ENTRYPOINT ["java", "-jar", "/app.jar"]
FROM openjdk:11-jdk
ENV LC_ALL=C.UTF-8
COPY . .
RUN chmod +x ./gradlew
RUN chmod +x ./code-review-0.0.1-SNAPSHOT.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "./code-review-0.0.1-SNAPSHOT.jar"]