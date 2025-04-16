# Usar uma imagem base do OpenJDK
FROM openjdk:17-jdk-slim

# Definir o diretório de trabalho no container
WORKDIR /app

# Copiar o arquivo JAR gerado pelo Maven para o container
COPY target/tax-calculator-API-0.0.1-SNAPSHOT.jar /app/app.jar

# Expor a porta usada pela aplicação
EXPOSE 8080

# Configurar variáveis de ambiente para o Spring Boot
ENV SPRING_PROFILES_ACTIVE=prod

# Comando para iniciar a aplicação
CMD ["java", "-jar", "/app/app.jar"]