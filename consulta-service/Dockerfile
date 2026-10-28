# ---------- Etapa 1: compilar ----------
# Aqui corre Maven: genera los DTOs desde consulta-api.yaml (contract-first) y arma el .jar.
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
# La cache de ~/.m2 hace que los builds siguientes no vuelvan a descargar las dependencias.
RUN --mount=type=cache,target=/root/.m2 mvn -B -ntp -DskipTests package

# ---------- Etapa 2: ejecutar ----------
# Imagen final: solo Java (sin Maven ni codigo fuente), mas pequena y con menos superficie de ataque.
FROM eclipse-temurin:17-jre
# El servicio NO corre como root: si alguien lo vulnerara, no tendria permisos de administrador en el contenedor.
RUN useradd --system --uid 10001 --no-create-home appuser
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
USER appuser
EXPOSE 8081
# La JVM respeta el limite de memoria del contenedor.
ENV JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=75"
ENTRYPOINT ["java", "-jar", "app.jar"]
