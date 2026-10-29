# JRE 17, como en los ejemplos de las clases de contenedores.
FROM eclipse-temurin:17-jre
WORKDIR /app
# El JAR se genera antes usando: mvn clean package -DskipTests
COPY target/Lab7_20226066-1.0.0.jar app.jar
EXPOSE 8080
# DB_URL, DB_USER y DB_PASSWORD se reciben como variables de entorno.
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
