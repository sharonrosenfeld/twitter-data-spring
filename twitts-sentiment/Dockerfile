FROM openjdk:8-jdk-alpine

ARG JAR_FILE=./target/twitts-sentiment-1.0-SNAPSHOT.jar
ARG APP_LOCATION=/home/sharon/app

COPY ${JAR_FILE} ${APP_LOCATION}/twitts-sentiment.jar

WORKDIR ${APP_LOCATION}

ENTRYPOINT ["java","-jar","twitts-sentiment.jar"]