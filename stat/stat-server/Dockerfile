FROM amazoncorretto:21-alpine3.20-jdk
COPY target/*.jar StatServer.jar
ENTRYPOINT ["java","-jar","/StatServer.jar"]
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://stats-db:5432/stats_db
ENV POSTGRES_USER=admin
ENV POSTGRES_PASSWORD=admin