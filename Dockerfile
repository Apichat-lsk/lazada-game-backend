# Stage 1: Build
FROM maven:3.9.3-eclipse-temurin-17 as builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /app/target/lazada-game-0.0.1-SNAPSHOT.jar app.jar
ENV TZ=Asia/Bangkok
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
