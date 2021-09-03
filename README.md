# spring-cloud-stream-event-routing-cloudevents

The goal of this project is to play with [`Spring Cloud Stream Event Routing`](https://docs.spring.io/spring-cloud-stream/docs/current/reference/html/spring-cloud-stream.html#_event_routing) and [`CloudEvents`](https://cloudevents.io/). For it, we will implement a producer and consumer of `news` & `alert` events.

## Applications

- ### producer-service

  [`Spring Boot`](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/) application that exposes a REST API to submit `news` & `alert` events.

- ### consumer-service

  `Spring Boot` application that consumes the `news` & `alert` events published by `producer-service`.

## Prerequisites

- [`Java 11+`](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)
- [`Docker`](https://www.docker.com/)
- [`Docker-Compose`](https://docs.docker.com/compose/install/)

## Start environment

- Open a terminal and inside `spring-cloud-stream-event-routing-cloudevents` root folder run
  ```
  docker-compose up -d
  ```

- Wait until all containers are Up (healthy). You can check their status by running
  ```
  docker-compose ps
  ```

## Running Applications with Maven

- **producer-service**

  - In a terminal, make sure you are in `spring-cloud-stream-event-routing-cloudevents` root folder
  - Run the command below to start the application
    ```
    ./mvnw clean spring-boot:run --projects producer-service
    ```

- **consumer-service**

  - Open a new terminal and navigate to `spring-cloud-stream-event-routing-cloudevents` root folder
  - Run the command below to start the application
    ```
    ./mvnw clean spring-boot:run --projects consumer-service
    ```

## Playing around

Submit the following POST requests to `producer-service` and check the logs in `consumer-service`

> **Note:** [HTTPie](https://httpie.org/) is being used in the calls bellow

- **news**
  ```
  http :9080/api/news/dw titel="Berliner Untergrundstreik"
  http :9080/api/news/cnn title="NYC subway strike"
  http :9080/api/news/rai titolo="Sciopero della metropolitana di Roma"
  ```

- **alert**
  ```
  http :9080/api/alerts/earthquake richterScale=5.5 epicenterLat=37.7840781 epicenterLon=-25.7977037
  http :9080/api/alerts/weather message="Thunderstorm in Berlin"
  ```

## Shutdown

- To stop applications, go to the terminals where they are running and press `Ctrl+C`
- To stop and remove docker-compose containers, network and volumes, go to a terminal and, inside `spring-cloud-stream-event-routing-cloudevents` root folder, run the following command
  ```
  docker-compose down -v
  ```

## Running Test Cases

- In a terminal, make sure you are inside `spring-cloud-stream-event-routing-cloudevents` root folder
- Execute the following Maven command to run `producer-service` and `consumer-service` test cases
  ```
  ./mvnw clean test
  ```
  In order to run the test cases of each service separately
  ```
  ./mvnw clean test --projects producer-service
  ./mvnw clean test --projects consumer-service
  ```

## References

- https://docs.spring.io/spring-cloud-stream/docs/current/reference/html/spring-cloud-stream.html
- https://stackoverflow.com/questions/61135632/spring-cloud-function-separate-routing-expression-for-different-consumer