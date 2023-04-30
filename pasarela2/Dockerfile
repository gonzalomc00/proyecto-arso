# Debe construirse desde la carpeta que contiene a los dos proyectos
# Por ejemplo: docker build -t bookle-rest:v2 -f bookle-rest/Dockerfile-2 .
FROM maven:3.8.5-jdk-11 AS builder

WORKDIR /app-base/
COPY usuarios-pasarela/pom.xml .
RUN mvn -e -B dependency:resolve
COPY usuarios-pasarela/src ./src
RUN mvn install

WORKDIR /app/
COPY pasarela2/pom.xml .
RUN mvn -e -B dependency:resolve
COPY pasarela2/src ./src
RUN mvn package


FROM eclipse-temurin:17-jdk-alpine
COPY target/*.jar ROOT.jar
ENTRYPOINT ["java","-jar","/ROOT.jar"]


EXPOSE 8090

