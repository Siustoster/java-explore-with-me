FROM amazoncorretto:21-alpine3.20-jdk
COPY target/*.jar MainService.jar
ENTRYPOINT ["java","-jar","/MainService.jar"]
