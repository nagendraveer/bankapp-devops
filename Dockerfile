FROM eclipse-temurin:17.0.13_11-jre-alpine
RUN apk add --no-cache wget
WORKDIR /app
COPY target/bankapp-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8081
HEALTHCHECK --interval=30s --timeout=10s --retries=3 \
  CMD wget -qO- http://localhost:8081/actuator/health || exit 1
ENTRYPOINT ["java", "-jar", "app.jar"]
