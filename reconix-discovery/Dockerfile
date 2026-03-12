FROM eclipse-temurin:21-jre-alpine

LABEL maintainer="Rafael Santana"
LABEL description="Reconix Discovery Server (Eureka)"

WORKDIR /app

COPY target/*.jar app.jar

EXPOSE 8761

HEALTHCHECK --interval=30s --timeout=10s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8761/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]