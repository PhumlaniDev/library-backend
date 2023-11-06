FROM eclipse-temurin:17-jdk-jammy AS base
WORKDIR /app
COPY mvnw pom.xml ./
RUN ./mvnw dependency:resolve
COPY src ./src

FROM base AS development
CMD ["./mvnw", "spring-boot:run", "-Dspring-boot.run.profile=postgres"]

FROM base AS build
RUN ./mvnw package

FROM eclipse-temurin:17-jdk-jammy AS config
WORKDIR /app/config
COPY src/main/resources/library-6f005-firebase-adminsdk-17zis-287127e243.json ./


FROM eclipse-temurin:17-jdk-jammy AS production
EXPOSE 7000
COPY --from=build /app/target/*.jar /app.jar

# Copy configuration from the config stage
COPY --from=config /app/config/library-6f005-firebase-adminsdk-17zis-287127e243.json /app/config/
CMD ["java", "-jar", "app.jar"]