#FROM openjdk:8-jdk-alpine
FROM openjdk:8-jre-alpine

RUN apk --no-cache add curl

ARG JAR_FILE=./target/twitts-storage-1.0-SNAPSHOT.jar
ARG APP_LOCATION=/home/sharon/app

COPY ${JAR_FILE} ${APP_LOCATION}/twitts-storage.jar
COPY ./start.sh ${APP_LOCATION}/start.sh

WORKDIR ${APP_LOCATION}


#ENTRYPOINT ["java","-jar","twitts-storage.jar"]
ENTRYPOINT ./start.sh