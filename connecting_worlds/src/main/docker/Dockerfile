FROM openjdk:11.0.14-jre

RUN mkdir /app
COPY target/*-shaded.jar /app/app.jar

CMD ["java", "-jar", "/app/app.jar"]