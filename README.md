The backend server is designed within SpringBoot 3.2.1 (latest version 12/21/2023) to operate with Java 17, utilizing the latest features and enhancements offered by the language. The project is built and managed using Maven 3.2.1, ensuring efficient dependency management and build processes. To facilitate data storage and retrieval, the backend relies on MySQL, and it assumes that MySQL is installed and configured on the environment where the server is deployed. This combination of Java 17, Maven 3.2.0, and MySQL provides a robust foundation for the backend server, offering both the power of a modern Java runtime and the convenience of widely-used build and dependency management tools.

This application gives you an idea how to build your own backend microservice related to security. It generates JWT token and provides you login/register API

Requirement:

- JDK 17
- Apache Maven 3.8.1

Installation:

Open terminal/command prompt and execute command to start server:

- `mvn clean spring-boot:run`

For Sonar download docker and sonar image to start sonar server. Following command will execute and analyze code coverage

- `mvn clean verify sonar:sonar -Dsonar.token=YOUR-TOKEN-HERE`

For more detail about SpringBoot 3.2.1 visit:

https://spring.io/blog/2023/12/21/spring-boot-3-2-1-available-now/
