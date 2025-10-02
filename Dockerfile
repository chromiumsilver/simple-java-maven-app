FROM amazoncorretto:17-alpine

EXPOSE 8080
WORKDIR /usr/app
COPY ./target/my-app-*.jar /usr/app/app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]