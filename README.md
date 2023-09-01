# spring-cloud-stream-event-routing-cloudevents

The goal of this project is to play with [`Spring Cloud Stream Event Routing`](https://docs.spring.io/spring-cloud-stream/docs/current/reference/html/spring-cloud-stream.html#_event_routing) and [`CloudEvents`](https://cloudevents.io/). For it, we will implement a producer and consumer of `news` & `alert` events.

## Proof-of-Concepts & Articles

On [ivangfr.github.io](https://ivangfr.github.io), I have compiled my Proof-of-Concepts (PoCs) and articles. You can easily search for the technology you are interested in by using the filter. Who knows, perhaps I have already implemented a PoC or written an article about what you are looking for.

## Additional Readings

- \[**Medium**\] [**How to Route CloudEvents messages with Spring Cloud Stream**](https://medium.com/@ivangfr/how-to-route-cloudevents-messages-with-spring-cloud-stream-3cf7a5ab4e17)

## Project Diagram

![project-diagram](documentation/project-diagram.jpeg)

## Applications

- ### producer-service

  [`Spring Boot`](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/) application that exposes a REST API to submit `news` & `alert` events.

  Endpoints
  ```
  POST /api/news/cnn {"title":"..."}
  POST /api/news/dw {"titel":"..."}
  POST /api/news/rai {"titolo":"..."}
  POST /api/alerts/earthquake {"richterScale":"...", "epicenterLat":"...", "epicenterLon":"..."}
  POST /api/alerts/weather {"message":"..."}
  ```

- ### consumer-service

  `Spring Boot` application that consumes the `news` & `alert` events published by `producer-service`.

## Prerequisites

- [`Java 17+`](https://www.oracle.com/java/technologies/downloads/#java17)
- [`Docker`](https://www.docker.com/)

## Start Environment

- Open a terminal and inside `spring-cloud-stream-event-routing-cloudevents` root folder run
  ```
  docker compose up -d
  ```

- Wait for Docker containers to be up and running. To check it, run
  ```
  docker compose ps
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

## Running Applications as Docker containers

- ### Build Docker Images

  - In a terminal, make sure you are inside `spring-cloud-stream-event-routing-cloudevents` root folder
  - Run the following script to build the Docker images
    - JVM
      ```
      ./docker-build.sh
      ```
    - Native
      ```
      ./docker-build.sh native
      ```

- ### Environment Variables

  - **producer-service**

    | Environment Variable | Description                                                             |
    |----------------------|-------------------------------------------------------------------------|
    | `KAFKA_HOST`         | Specify host of the `Kafka` message broker to use (default `localhost`) |
    | `KAFKA_PORT`         | Specify port of the `Kafka` message broker to use (default `29092`)     |

  - **consumer-service**

    | Environment Variable | Description                                                             |
    |----------------------|-------------------------------------------------------------------------|
    | `KAFKA_HOST`         | Specify host of the `Kafka` message broker to use (default `localhost`) |
    | `KAFKA_PORT`         | Specify port of the `Kafka` message broker to use (default `29092`)     |

- ### Run Docker Containers

  - **producer-service**
    
    Run the following command in a terminal
    ```
    docker run --rm --name producer-service -p 9080:9080 \
      -e KAFKA_HOST=kafka -e KAFKA_PORT=9092 \
      --network=spring-cloud-stream-event-routing-cloudevents_default \
      ivanfranchin/producer-service:1.0.0
    ```

  - **consumer-service**
    
    Open a new terminal and run the following command
    ```
    docker run --rm --name consumer-service -p 9081:9081 \
      -e KAFKA_HOST=kafka -e KAFKA_PORT=9092 \
      --network=spring-cloud-stream-event-routing-cloudevents_default \
      ivanfranchin/consumer-service:1.0.0
    ```

## Playing around

In a terminal, submit the following POST requests to `producer-service` and check its logs and `consumer-service` logs

> **Note**: [HTTPie](https://httpie.org/) is being used in the calls bellow

- **news**
  ```
  http :9080/api/news/cnn title="NYC subway strike"
  http :9080/api/news/dw titel="Berliner Untergrundstreik"
  http :9080/api/news/rai titolo="Sciopero della metropolitana di Roma"
  ```

- **alerts**
  ```
  http :9080/api/alerts/earthquake richterScale=5.5 epicenterLat=37.7840781 epicenterLon=-25.7977037
  http :9080/api/alerts/weather message="Thunderstorm in Berlin"
  ```

## Useful links

- **Kafdrop**

  `Kafdrop` can be accessed at http://localhost:9000

## Shutdown

- To stop applications, go to the terminals where they are running and press `Ctrl+C`
- To stop and remove docker compose containers, network and volumes, go to a terminal and, inside `spring-cloud-stream-event-routing-cloudevents` root folder, run the following command
  ```
  docker compose down -v
  ```

## Running Test Cases

In a terminal, make sure you are inside `spring-cloud-stream-event-routing-cloudevents` root folder

- **producer-service**
  ```
  ./mvnw clean test --projects producer-service
  ```

- **consumer-service**
  ```
  ./mvnw clean test --projects consumer-service
  ```

## Cleanup

To remove the Docker images created by this project, go to a terminal and inside `spring-cloud-stream-event-routing-cloudevents` root folder, run the following script
```
./remove-docker-images.sh
```

## References

- https://docs.spring.io/spring-cloud-stream/docs/current/reference/html/spring-cloud-stream.html
- https://stackoverflow.com/questions/61135632/spring-cloud-function-separate-routing-expression-for-different-consumer

## Issues

Unable to build the `producer-service` Docker Native image. The following exception is thrown:
```
Exception in thread "main" org.springframework.boot.context.properties.bind.BindException: Failed to bind properties under 'spring.cloud.stream.bindings.news-out-0.producer.partition-key-expression' to org.springframework.expression.Expression
	at org.springframework.boot.context.properties.bind.Binder.handleBindError(Binder.java:392)
	at org.springframework.boot.context.properties.bind.Binder.bind(Binder.java:352)
	at org.springframework.boot.context.properties.bind.Binder.lambda$bindDataObject$4(Binder.java:478)
	at org.springframework.boot.context.properties.bind.JavaBeanBinder.bind(JavaBeanBinder.java:99)
	at org.springframework.boot.context.properties.bind.JavaBeanBinder.bind(JavaBeanBinder.java:87)
	at org.springframework.boot.context.properties.bind.JavaBeanBinder.bind(JavaBeanBinder.java:63)
	at org.springframework.boot.context.properties.bind.Binder.lambda$bindDataObject$5(Binder.java:482)
	at org.springframework.boot.context.properties.bind.Binder$Context.withIncreasedDepth(Binder.java:596)
	at org.springframework.boot.context.properties.bind.Binder$Context.withDataObject(Binder.java:582)
	at org.springframework.boot.context.properties.bind.Binder.bindDataObject(Binder.java:480)
	at org.springframework.boot.context.properties.bind.Binder.bindObject(Binder.java:419)
	at org.springframework.boot.context.properties.bind.Binder.bind(Binder.java:348)
	at org.springframework.boot.context.properties.bind.Binder.lambda$bindDataObject$4(Binder.java:478)
	at org.springframework.boot.context.properties.bind.JavaBeanBinder.bind(JavaBeanBinder.java:99)
	at org.springframework.boot.context.properties.bind.JavaBeanBinder.bind(JavaBeanBinder.java:87)
	at org.springframework.boot.context.properties.bind.JavaBeanBinder.bind(JavaBeanBinder.java:63)
	at org.springframework.boot.context.properties.bind.Binder.lambda$bindDataObject$5(Binder.java:482)
	at org.springframework.boot.context.properties.bind.Binder$Context.withIncreasedDepth(Binder.java:596)
	at org.springframework.boot.context.properties.bind.Binder$Context.withDataObject(Binder.java:582)
	at org.springframework.boot.context.properties.bind.Binder.bindDataObject(Binder.java:480)
	at org.springframework.boot.context.properties.bind.Binder.bindObject(Binder.java:419)
	at org.springframework.boot.context.properties.bind.Binder.bind(Binder.java:348)
	at org.springframework.boot.context.properties.bind.Binder.lambda$bindAggregate$1(Binder.java:440)
	at org.springframework.boot.context.properties.bind.Binder$Context.withSource(Binder.java:567)
	at org.springframework.boot.context.properties.bind.Binder.lambda$bindAggregate$2(Binder.java:441)
	at org.springframework.boot.context.properties.bind.AggregateElementBinder.bind(AggregateElementBinder.java:39)
	at org.springframework.boot.context.properties.bind.MapBinder$EntryBinder.lambda$bindEntries$0(MapBinder.java:158)
	at java.base/java.util.HashMap.computeIfAbsent(HashMap.java:1220)
	at org.springframework.boot.context.properties.bind.MapBinder$EntryBinder.bindEntries(MapBinder.java:158)
	at org.springframework.boot.context.properties.bind.MapBinder.bindAggregate(MapBinder.java:69)
	at org.springframework.boot.context.properties.bind.AggregateBinder.bind(AggregateBinder.java:56)
	at org.springframework.boot.context.properties.bind.Binder.lambda$bindAggregate$3(Binder.java:443)
	at org.springframework.boot.context.properties.bind.Binder$Context.withIncreasedDepth(Binder.java:596)
	at org.springframework.boot.context.properties.bind.Binder.bindAggregate(Binder.java:443)
	at org.springframework.boot.context.properties.bind.Binder.bindObject(Binder.java:404)
	at org.springframework.boot.context.properties.bind.Binder.bind(Binder.java:348)
	at org.springframework.boot.context.properties.bind.Binder.lambda$bindDataObject$4(Binder.java:478)
	at org.springframework.boot.context.properties.bind.JavaBeanBinder.bind(JavaBeanBinder.java:99)
	at org.springframework.boot.context.properties.bind.JavaBeanBinder.bind(JavaBeanBinder.java:87)
	at org.springframework.boot.context.properties.bind.JavaBeanBinder.bind(JavaBeanBinder.java:63)
	at org.springframework.boot.context.properties.bind.Binder.lambda$bindDataObject$5(Binder.java:482)
	at org.springframework.boot.context.properties.bind.Binder$Context.withIncreasedDepth(Binder.java:596)
	at org.springframework.boot.context.properties.bind.Binder$Context.withDataObject(Binder.java:582)
	at org.springframework.boot.context.properties.bind.Binder.bindDataObject(Binder.java:480)
	at org.springframework.boot.context.properties.bind.Binder.bindObject(Binder.java:419)
	at org.springframework.boot.context.properties.bind.Binder.bind(Binder.java:348)
	at org.springframework.boot.context.properties.bind.Binder.bind(Binder.java:337)
	at org.springframework.boot.context.properties.bind.Binder.bind(Binder.java:267)
	at org.springframework.boot.context.properties.bind.Binder.bind(Binder.java:228)
	at org.springframework.cloud.stream.binder.BinderChildContextInitializer.createBindingServiceProperties(BinderChildContextInitializer.java:110)
	at org.springframework.cloud.stream.binder.BinderChildContextInitializer.processAheadOfTime(BinderChildContextInitializer.java:95)
	at org.springframework.beans.factory.aot.BeanDefinitionMethodGeneratorFactory.getAotContributions(BeanDefinitionMethodGeneratorFactory.java:151)
	at org.springframework.beans.factory.aot.BeanDefinitionMethodGeneratorFactory.getBeanDefinitionMethodGenerator(BeanDefinitionMethodGeneratorFactory.java:99)
	at org.springframework.beans.factory.aot.BeanDefinitionMethodGeneratorFactory.getBeanDefinitionMethodGenerator(BeanDefinitionMethodGeneratorFactory.java:115)
	at org.springframework.beans.factory.aot.BeanRegistrationsAotProcessor.processAheadOfTime(BeanRegistrationsAotProcessor.java:49)
	at org.springframework.beans.factory.aot.BeanRegistrationsAotProcessor.processAheadOfTime(BeanRegistrationsAotProcessor.java:37)
	at org.springframework.context.aot.BeanFactoryInitializationAotContributions.getContributions(BeanFactoryInitializationAotContributions.java:67)
	at org.springframework.context.aot.BeanFactoryInitializationAotContributions.<init>(BeanFactoryInitializationAotContributions.java:49)
	at org.springframework.context.aot.BeanFactoryInitializationAotContributions.<init>(BeanFactoryInitializationAotContributions.java:44)
	at org.springframework.context.aot.ApplicationContextAotGenerator.lambda$processAheadOfTime$0(ApplicationContextAotGenerator.java:58)
	at org.springframework.context.aot.ApplicationContextAotGenerator.withCglibClassHandler(ApplicationContextAotGenerator.java:67)
	at org.springframework.context.aot.ApplicationContextAotGenerator.processAheadOfTime(ApplicationContextAotGenerator.java:53)
	at org.springframework.context.aot.ContextAotProcessor.performAotProcessing(ContextAotProcessor.java:106)
	at org.springframework.context.aot.ContextAotProcessor.doProcess(ContextAotProcessor.java:84)
	at org.springframework.context.aot.ContextAotProcessor.doProcess(ContextAotProcessor.java:49)
	at org.springframework.context.aot.AbstractAotProcessor.process(AbstractAotProcessor.java:82)
	at org.springframework.boot.SpringApplicationAotProcessor.main(SpringApplicationAotProcessor.java:80)
Caused by: org.springframework.core.convert.ConverterNotFoundException: No converter found capable of converting from type [java.lang.String] to type [@com.fasterxml.jackson.databind.annotation.JsonSerialize org.springframework.expression.Expression]
	at org.springframework.boot.context.properties.bind.BindConverter.convert(BindConverter.java:118)
	at org.springframework.boot.context.properties.bind.BindConverter.convert(BindConverter.java:100)
	at org.springframework.boot.context.properties.bind.BindConverter.convert(BindConverter.java:92)
	at org.springframework.boot.context.properties.bind.Binder.bindProperty(Binder.java:464)
	at org.springframework.boot.context.properties.bind.Binder.bindObject(Binder.java:408)
	at org.springframework.boot.context.properties.bind.Binder.bind(Binder.java:348)
	... 65 more
```