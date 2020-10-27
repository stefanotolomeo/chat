FROM openjdk:8-jdk-alpine

RUN addgroup -S spring && adduser -S spring -G spring

RUN mkdir -p /chat-message/logs
RUN chown spring /chat-message/logs

USER spring:spring

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} chat-message.jar

ENTRYPOINT ["java","-jar","/chat-message.jar"]