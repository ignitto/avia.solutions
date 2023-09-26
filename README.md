# avia.solutions app

[![Coverage Status](https://coveralls.io/repos/github/ignitto/avia.solutions/badge.svg?branch=main)](https://coveralls.io/github/ignitto/avia.solutions?branch=main)

Avia solutions Spring Boot Kotlin flight registration system.

## Requirements

For building and running the application you need:

- [JDK 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- [Maven 3](https://maven.apache.org)

## Running the application locally

There are several ways to run a Spring Boot application on your local machine. One way is to execute the `main` method in the `com.ignas.avia.solutions.FlightRegistrationSystemApplication` class from your IDE.

Alternatively you can use the [Spring Boot Maven plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html) like so:

```shell
mvn spring-boot:run
```

## Running on Docker

The easiest way to run application on Docker

```shell
docker build -t avia.solutions . 
docker run -p 8080:8080 avia.solutions 
```

## SWAGGER

Run Swagger locally

```shell
http://localhost:8080/swagger-ui/index.html
```

## Copyright
Ignas 2023