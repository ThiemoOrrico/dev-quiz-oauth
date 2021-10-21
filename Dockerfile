FROM openjdk:17

ENV ENVIRONMENT=prod

MAINTAINER Christopher Siem <christopher.siem@neuefische.de>

ADD backend/target/devquiz.jar app.jar

CMD [ "sh", "-c", "java -Dserver.port=$PORT -Dspring.data.mongodb.uri=$MONGO_DB_URI -jar /app.jar" ]