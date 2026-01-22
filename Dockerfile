FROM eclipse-temurin:21-jre

WORKDIR /app

# Copier le JAR déjà buildé
COPY target/*.jar app.jar

# Profil Spring par défaut (override possible)
ENV SPRING_PROFILES_ACTIVE=prod

# Port exposé par Spring Boot
EXPOSE 8080

# Lancement de l'application
ENTRYPOINT ["java", "-jar", "app.jar"]
