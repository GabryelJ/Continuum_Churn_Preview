# Estágio 1: Build
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copia apenas o pom.xml para baixar as dependências (otimiza o cache do Docker)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copia o código fonte e gera o jar
COPY src ./src
RUN mvn clean package -DskipTests

# Estágio 2: Runtime
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Copia o jar gerado no estágio anterior
COPY --from build /app/target/*.jar app.jar

# Expõe a porta padrão do Spring Boot
EXPOSE 8080

# Comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]