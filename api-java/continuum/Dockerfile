# Estágio 1: Build
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copia apenas o pom.xml para aproveitar cache
COPY pom.xml .
RUN mvn dependency:go-offline

# Copia o código fonte e gera o jar
COPY src ./src
RUN mvn clean package -DskipTests

# Estágio 2: Runtime
FROM eclipse-temurin:17-jre-jammy
WORKDIR /continuum

# Copia o jar do estágio de build
COPY --from=build /app/target/*.jar app.jar

# Expõe a porta padrão do Spring Boot
EXPOSE 8080

# Comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
