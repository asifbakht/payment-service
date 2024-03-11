FROM openjdk:23-jdk-oraclelinux7
MAINTAINER Asif Bakht
COPY target/payment-0.0.1-SNAPSHOT.jar payment.jar
EXPOSE 9999
ENTRYPOINT ["java","-jar","/payment.jar"]


