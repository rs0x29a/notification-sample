FROM alpine:3.14

RUN apk update \
  && apk upgrade \
  && apk add --update openjdk11 tzdata curl unzip bash \
  && rm -rf /var/cache/apk/*

ENV LANG ru_RU.UTF-8

ARG JAR_FILE=build/libs/notification-route-1.0.0.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT java -Dfile.encoding=UTF-8 -jar app.jar
EXPOSE 8080
