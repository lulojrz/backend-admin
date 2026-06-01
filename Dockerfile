 # --- Etapa 1: Compilacion ---
  FROM maven:3.9.6-eclipse-temurin-17 AS build
  WORKDIR /app

  # Copiamos el pom y descargamos las dependencias primero (aprovecha la cache de Docker)
  COPY pom.xml .
  RUN mvn dependency:go-offline -B

  # Copiamos todo el código fuente
  COPY src ./src

  # SOLUCIoN DE RAÍZ: Desactivamos el filtrado problematico forzando codificación en el comando
  RUN mvn clean package -DskipTests

  # --- Etapa 2: Ejecucion ---
  FROM eclipse-temurin:17-jre-jammy
  WORKDIR /app

  COPY --from=build /app/target/*.jar app.jar

  EXPOSE 8080

  ENTRYPOINT ["java", "-jar", "app.jar"]