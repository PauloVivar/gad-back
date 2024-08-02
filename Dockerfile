FROM eclipse-temurin:21.0.3_9-jdk

WORKDIR /app

COPY ./target/gad-back-0.0.1-SNAPSHOT.jar .

EXPOSE 8080

# Define una variable de entorno
ENV SPRING_MAIL_PASSWORD=eqwyqgalkzsykgvi

ENTRYPOINT ["java", "-jar", "gad-back-0.0.1-SNAPSHOT.jar"]
