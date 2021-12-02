FROM maven:3.8.4-jdk-11-slim AS BUILD
ENV APP_HOME=/usr/app/
WORKDIR $APP_HOME
COPY pom.xml $APP_HOME
RUN mvn dependency:resolve dependency:resolve-plugins
COPY . $APP_HOME
RUN mvn compile package

FROM adoptopenjdk/openjdk11:alpine-jre
ENV ARTIFACT_NAME=soc-network-rest-0.0.1-SNAPSHOT.jar
ENV APP_HOME=/usr/app/

WORKDIR $APP_HOME
COPY --from=BUILD $APP_HOME/target/$ARTIFACT_NAME .

EXPOSE 8080
ENTRYPOINT exec java -jar ${ARTIFACT_NAME}
