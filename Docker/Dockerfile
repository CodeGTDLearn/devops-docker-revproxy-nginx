# STAGE 01: Build the WebApp
FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /app

# Docker-Compose setou o Context[root] na pasta anterior,
# a pasta Docker, logo 'pom' e 'src' sao buscados na [root]
COPY pom.xml ./
COPY src ./src/

#VAADIN:
RUN mvn clean package -Pproduction


# STAGE 02: Run the WebApp
FROM openjdk:17-alpine
WORKDIR /app
COPY --from=build /app/target/devops-docker-revproxy-nginx-1.0-SNAPSHOT.jar ./revprox-nginx.jar
EXPOSE 8080
CMD ["java", "-jar", "revprox-nginx.jar"]