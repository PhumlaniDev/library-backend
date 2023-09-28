FROM eclipse-temurin:17-jdk-jammy AS base
WORKDIR /app
COPY .mvn .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:resolve
COPY src ./src

FROM base AS development
CMD ["./mvnw", "spring-boot:run", "-Dspring-boot.run.profile=postgres"]

FROM base AS build
RUN ./mvnw package

FROM eclipse-temurin:17-jdk-jammy AS config
WORKDIR /app/config
COPY src/main/resources/book-library-5a143-firebase-adminsdk-budnm-73dd7544f8.json ./


FROM eclipse-temurin:17-jdk-jammy AS production
EXPOSE 8080
COPY --from=build /app/target/*.jar /app.jar

# Copy configuration from the config stage
COPY --from=config src/main/resources/book-library-5a143-firebase-adminsdk-budnm-73dd7544f8.json /app/config/
CMD ["java", "-jar", "app.jar"]