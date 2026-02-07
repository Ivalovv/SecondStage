FROM eclipse-temurin:21-jdk
WORKDIR /app
ARG JAR_FILE=target/*.jar
COPY target/*-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app/app.jar"]