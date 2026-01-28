# === Stage 1 : Construction avec Maven ===
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

# Optimisation : Copier uniquement le pom.xml pour télécharger les dépendances (Cache Docker)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copier les sources et construire
COPY src ./src
RUN mvn clean package -DskipTests

# === Stage 2 : Image d'exécution légère ===
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copier le .jar depuis l'image de build
COPY --from=build /app/target/backend-0.0.1-SNAPSHOT.jar app.jar

# Exposer le port d'ecoute (8081)
EXPOSE 8081

ENTRYPOINT ["sh", "-c", "java -jar app.jar"]