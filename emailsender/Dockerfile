FROM maven:3.8.4-openjdk-17 as builder

WORKDIR /app

COPY . /app

RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY --from=builder /app/target/emailsender.jar /app/emailsender.jar

ENV SERVER_PORT=61002

EXPOSE $SERVER_PORT

CMD ["java", "-jar", "emailsender.jar", "--server.port=${SERVER_PORT}"]
