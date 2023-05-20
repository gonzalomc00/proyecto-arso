# Debe construirse desde la carpeta que contiene a los dos proyectos
# Por ejemplo: docker build -t bookle-rest:v2 -f bookle-rest/Dockerfile-2 .
FROM maven:3.8.5-jdk-11 AS builder

WORKDIR /app-base/
COPY restaurantes/pom.xml .
RUN mvn -e -B dependency:resolve
COPY restaurantes/src ./src
RUN mvn install

WORKDIR /app/
COPY restaurantes-rest/pom.xml .
RUN mvn -e -B dependency:resolve
COPY restaurantes-rest/src ./src

# Establecer variable de entorno antes de la construcción
ENV OPINIONES_API_URL=http://opiniones:5000/api/

RUN mvn package

FROM tomcat:9.0.58-jdk11
WORKDIR /usr/local/tomcat/webapps/
COPY --from=builder /app/target/restaurantes-rest.war ROOT.war

# Establecer variable de entorno en el contenedor
ENV OPINIONES_API_URL=http://opiniones:5000/api/

EXPOSE 8080
CMD ["catalina.sh", "run"]
