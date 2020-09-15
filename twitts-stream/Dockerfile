FROM openjdk:8-jdk-alpine

ARG JAR_FILE=./target/twitts-stream-1.0-SNAPSHOT.jar
ARG APP_LOCATION=/home/sharon/app

COPY ${JAR_FILE} ${APP_LOCATION}/twitts-stream.jar

WORKDIR ${APP_LOCATION}

ENTRYPOINT ["java","-jar","twitts-stream.jar"]