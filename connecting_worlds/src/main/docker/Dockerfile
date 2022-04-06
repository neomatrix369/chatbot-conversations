FROM adoptopenjdk/openjdk11:x86_64-alpine-jre-11.0.14.1_1

RUN mkdir /app
COPY target/*-shaded.jar /app/app.jar

CMD ["java", "-jar", "/app/app.jar"]