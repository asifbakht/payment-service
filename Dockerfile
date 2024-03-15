FROM openjdk:23-jdk-oraclelinux7
MAINTAINER Asif Bakht
COPY target/payment-0.0.1-SNAPSHOT.jar payment.jar
ENV CONTAINER_PORT=9999
EXPOSE $CONTAINER_PORT
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=prod","/payment.jar"]


