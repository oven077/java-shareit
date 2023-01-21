#FROM amazoncorretto:17
#COPY target/*.jar shareit-gateway.jar
#ENTRYPOINT ["java","-jar","/shareit-gateway.jar"]
#
#EXPOSE 8080

FROM amazoncorretto:11-alpine-jdk
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]