# spring-cloud-stream-event-routing-cloudevents

The goal of this project is to play with [`Spring Cloud Stream Event Routing`](https://docs.spring.io/spring-cloud-stream/docs/current/reference/html/spring-cloud-stream.html#_event_routing) and [`CloudEvents`](https://cloudevents.io/). For it, we will implement a producer and consumer of `news` & `alert` events.

## Applications

- ### producer-service

  [`Spring Boot`](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/) application that exposes a REST API to submit `news` & `alert` events.

  Endpoints
  ```
  POST /api/news/cnn {"title": "..."}
  POST /api/news/dw {"titel": "..."}
  POST /api/news/rai {"titolo": "..."}
  POST /api/alert/earthquake {"richterScale": "...", "epicenterLat": "...", "epicenterLon": "..."}
  POST /api/alert/weather {"message": "..."}
  ```

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
    | -------------------- | ----------------------------------------------------------------------- |
    | `KAFKA_HOST`         | Specify host of the `Kafka` message broker to use (default `localhost`) |
    | `KAFKA_PORT`         | Specify port of the `Kafka` message broker to use (default `29092`)     |

  - **consumer-service**

    | Environment Variable | Description                                                             |
    | -------------------- | ----------------------------------------------------------------------- |
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

Submit the following POST requests to `producer-service` and check the logs in `consumer-service`

> **Note:** [HTTPie](https://httpie.org/) is being used in the calls bellow

- **news**
  ```
  http :9080/api/news/dw titel="Berliner Untergrundstreik"
  http :9080/api/news/cnn title="NYC subway strike"
  http :9080/api/news/rai titolo="Sciopero della metropolitana di Roma"
  ```

- **alerts**
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

## Cleanup

To remove the Docker images created by this project, go to a terminal and run the following commands
```
docker rmi ivanfranchin/producer-service:1.0.0
docker rmi ivanfranchin/consumer-service:1.0.0
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

## References

- https://docs.spring.io/spring-cloud-stream/docs/current/reference/html/spring-cloud-stream.html
- https://stackoverflow.com/questions/61135632/spring-cloud-function-separate-routing-expression-for-different-consumer

## Issues

When submitting, to `producer-service`, an invalid event according to `javax.validation.constraints`, like the field `titolo` in `CreateRAINewsRequest` must not be blank, for instance
```
$ http :9080/api/news/rai title="Sciopero della metropolitana di Roma"
```
the following exception is thrown
```
ERROR 1 --- [ctor-http-nio-2] o.s.w.s.adapter.HttpWebHandlerAdapter    : [30b7f679-5] 500 Server Error for HTTP POST "/api/news/rai"

org.springframework.core.codec.CodecException: Type definition error: [simple type, class org.springframework.validation.beanvalidation.SpringValidatorAdapter$ViolationFieldError]; nested exception is com.fasterxml.jackson.databind.exc.InvalidDefinitionException: No serializer found for class org.springframework.validation.beanvalidation.SpringValidatorAdapter$ViolationFieldError and no properties discovered to create BeanSerializer (to avoid exception, disable SerializationFeature.FAIL_ON_EMPTY_BEANS) (through reference chain: java.util.LinkedHashMap["errors"]->java.util.Collections$UnmodifiableRandomAccessList[0])
    at org.springframework.http.codec.json.AbstractJackson2Encoder.encodeValue(AbstractJackson2Encoder.java:226) ~[na:na]
    at org.springframework.http.codec.json.AbstractJackson2Encoder.lambda$encode$0(AbstractJackson2Encoder.java:150) ~[na:na]
    at reactor.core.publisher.FluxMapFuseable$MapFuseableSubscriber.onNext(FluxMapFuseable.java:113) ~[na:na]
    at reactor.core.publisher.Operators$ScalarSubscription.request(Operators.java:2398) ~[com.mycompany.producerservice.ProducerServiceApplication:3.4.9]
    at reactor.core.publisher.FluxMapFuseable$MapFuseableSubscriber.request(FluxMapFuseable.java:169) ~[na:na]
    at reactor.core.publisher.MonoSingle$SingleSubscriber.doOnRequest(MonoSingle.java:103) ~[na:na]
    at reactor.core.publisher.Operators$MonoInnerProducerBase.request(Operators.java:2731) ~[com.mycompany.producerservice.ProducerServiceApplication:3.4.9]
    at reactor.core.publisher.Operators$MultiSubscriptionSubscriber.set(Operators.java:2194) ~[com.mycompany.producerservice.ProducerServiceApplication:3.4.9]
    at reactor.core.publisher.Operators$MultiSubscriptionSubscriber.onSubscribe(Operators.java:2068) ~[com.mycompany.producerservice.ProducerServiceApplication:3.4.9]
    at reactor.core.publisher.MonoSingle$SingleSubscriber.onSubscribe(MonoSingle.java:115) ~[na:na]
    at reactor.core.publisher.FluxMapFuseable$MapFuseableSubscriber.onSubscribe(FluxMapFuseable.java:96) ~[na:na]
    at reactor.core.publisher.MonoJust.subscribe(MonoJust.java:55) ~[na:na]
    at reactor.core.publisher.InternalMonoOperator.subscribe(InternalMonoOperator.java:64) ~[na:na]
    at reactor.core.publisher.MonoFlatMap$FlatMapMain.onNext(MonoFlatMap.java:157) ~[com.mycompany.producerservice.ProducerServiceApplication:3.4.9]
    at reactor.core.publisher.FluxPeekFuseable$PeekFuseableSubscriber.onNext(FluxPeekFuseable.java:210) ~[na:na]
    at reactor.core.publisher.Operators$MonoSubscriber.complete(Operators.java:1816) ~[com.mycompany.producerservice.ProducerServiceApplication:3.4.9]
    at reactor.core.publisher.MonoFlatMap$FlatMapMain.onNext(MonoFlatMap.java:151) ~[com.mycompany.producerservice.ProducerServiceApplication:3.4.9]
    at reactor.core.publisher.FluxSwitchIfEmpty$SwitchIfEmptySubscriber.onNext(FluxSwitchIfEmpty.java:74) ~[na:na]
    at reactor.core.publisher.MonoNext$NextSubscriber.onNext(MonoNext.java:82) ~[com.mycompany.producerservice.ProducerServiceApplication:3.4.9]
    at reactor.core.publisher.FluxConcatArray$ConcatArraySubscriber.onNext(FluxConcatArray.java:201) ~[com.mycompany.producerservice.ProducerServiceApplication:3.4.9]
    at reactor.core.publisher.Operators$ScalarSubscription.request(Operators.java:2398) ~[com.mycompany.producerservice.ProducerServiceApplication:3.4.9]
    at reactor.core.publisher.FluxConcatArray$ConcatArraySubscriber.onSubscribe(FluxConcatArray.java:193) ~[com.mycompany.producerservice.ProducerServiceApplication:3.4.9]
    at reactor.core.publisher.MonoJust.subscribe(MonoJust.java:55) ~[na:na]
    at reactor.core.publisher.MonoDefer.subscribe(MonoDefer.java:52) ~[na:na]
    at reactor.core.publisher.Mono.subscribe(Mono.java:4338) ~[com.mycompany.producerservice.ProducerServiceApplication:3.4.9]
    at reactor.core.publisher.FluxConcatArray$ConcatArraySubscriber.onComplete(FluxConcatArray.java:255) ~[com.mycompany.producerservice.ProducerServiceApplication:3.4.9]
    at reactor.core.publisher.FluxConcatArray.subscribe(FluxConcatArray.java:78) ~[na:na]
    at reactor.core.publisher.Mono.subscribe(Mono.java:4338) ~[com.mycompany.producerservice.ProducerServiceApplication:3.4.9]
    at reactor.core.publisher.FluxOnErrorResume$ResumeSubscriber.onError(FluxOnErrorResume.java:103) ~[na:na]
    at reactor.core.publisher.FluxOnErrorResume$ResumeSubscriber.onError(FluxOnErrorResume.java:106) ~[na:na]
    at reactor.core.publisher.FluxOnAssembly$OnAssemblySubscriber.onError(FluxOnAssembly.java:393) ~[na:na]
    at reactor.core.publisher.Operators.error(Operators.java:198) ~[na:na]
    at reactor.core.publisher.MonoError.subscribe(MonoError.java:53) ~[na:na]
    at reactor.core.publisher.Mono.subscribe(Mono.java:4338) ~[com.mycompany.producerservice.ProducerServiceApplication:3.4.9]
    at reactor.core.publisher.FluxOnErrorResume$ResumeSubscriber.onError(FluxOnErrorResume.java:103) ~[na:na]
    at reactor.core.publisher.FluxOnAssembly$OnAssemblySubscriber.onError(FluxOnAssembly.java:393) ~[na:na]
    at reactor.core.publisher.FluxPeek$PeekSubscriber.onError(FluxPeek.java:222) ~[na:na]
    at reactor.core.publisher.FluxDoOnEach$DoOnEachSubscriber.onError(FluxDoOnEach.java:195) ~[na:na]
    at reactor.core.publisher.MonoFlatMap$FlatMapMain.onError(MonoFlatMap.java:172) ~[com.mycompany.producerservice.ProducerServiceApplication:3.4.9]
    at reactor.core.publisher.MonoFlatMap$FlatMapMain.secondError(MonoFlatMap.java:192) ~[com.mycompany.producerservice.ProducerServiceApplication:3.4.9]
    at reactor.core.publisher.MonoFlatMap$FlatMapInner.onError(MonoFlatMap.java:259) ~[com.mycompany.producerservice.ProducerServiceApplication:3.4.9]
    at reactor.core.publisher.FluxOnErrorResume$ResumeSubscriber.onError(FluxOnErrorResume.java:106) ~[na:na]
    at reactor.core.publisher.Operators.error(Operators.java:198) ~[na:na]
    at reactor.core.publisher.MonoError.subscribe(MonoError.java:53) ~[na:na]
    at reactor.core.publisher.Mono.subscribe(Mono.java:4338) ~[com.mycompany.producerservice.ProducerServiceApplication:3.4.9]
    at reactor.core.publisher.FluxOnErrorResume$ResumeSubscriber.onError(FluxOnErrorResume.java:103) ~[na:na]
    at reactor.core.publisher.FluxPeek$PeekSubscriber.onError(FluxPeek.java:222) ~[na:na]
    at reactor.core.publisher.FluxPeek$PeekSubscriber.onError(FluxPeek.java:222) ~[na:na]
    at reactor.core.publisher.MonoIgnoreThen$ThenIgnoreMain.onError(MonoIgnoreThen.java:270) ~[com.mycompany.producerservice.ProducerServiceApplication:3.4.9]
    at reactor.core.publisher.MonoFlatMap$FlatMapMain.onError(MonoFlatMap.java:172) ~[com.mycompany.producerservice.ProducerServiceApplication:3.4.9]
    at reactor.core.publisher.MonoZip$ZipInner.onError(MonoZip.java:350) ~[com.mycompany.producerservice.ProducerServiceApplication:3.4.9]
    at reactor.core.publisher.MonoPeekTerminal$MonoTerminalPeekSubscriber.onError(MonoPeekTerminal.java:258) ~[na:na]
    at reactor.core.publisher.Operators$MonoSubscriber.onError(Operators.java:1863) ~[com.mycompany.producerservice.ProducerServiceApplication:3.4.9]
    at reactor.core.publisher.FluxPeek$PeekSubscriber.onError(FluxPeek.java:222) ~[na:na]
    at reactor.core.publisher.FluxPeek$PeekSubscriber.onNext(FluxPeek.java:194) ~[na:na]
    at reactor.core.publisher.FluxSwitchIfEmpty$SwitchIfEmptySubscriber.onNext(FluxSwitchIfEmpty.java:74) ~[na:na]
    at reactor.core.publisher.FluxOnErrorResume$ResumeSubscriber.onNext(FluxOnErrorResume.java:79) ~[na:na]
    at reactor.core.publisher.Operators$MonoSubscriber.complete(Operators.java:1816) ~[com.mycompany.producerservice.ProducerServiceApplication:3.4.9]
    at reactor.core.publisher.MonoFlatMap$FlatMapMain.onNext(MonoFlatMap.java:151) ~[com.mycompany.producerservice.ProducerServiceApplication:3.4.9]
    at reactor.core.publisher.FluxContextWrite$ContextWriteSubscriber.onNext(FluxContextWrite.java:107) ~[na:na]
    at reactor.core.publisher.FluxMapFuseable$MapFuseableConditionalSubscriber.onNext(FluxMapFuseable.java:295) ~[na:na]
    at reactor.core.publisher.FluxFilterFuseable$FilterFuseableConditionalSubscriber.onNext(FluxFilterFuseable.java:337) ~[na:na]
    at reactor.core.publisher.Operators$MonoSubscriber.complete(Operators.java:1816) ~[com.mycompany.producerservice.ProducerServiceApplication:3.4.9]
    at reactor.core.publisher.MonoCollect$CollectSubscriber.onComplete(MonoCollect.java:159) ~[na:na]
    at reactor.core.publisher.FluxMap$MapSubscriber.onComplete(FluxMap.java:142) ~[na:na]
    at reactor.core.publisher.FluxPeek$PeekSubscriber.onComplete(FluxPeek.java:260) ~[na:na]
    at reactor.core.publisher.FluxMap$MapSubscriber.onComplete(FluxMap.java:142) ~[na:na]
    at reactor.netty.channel.FluxReceive.onInboundComplete(FluxReceive.java:400) ~[com.mycompany.producerservice.ProducerServiceApplication:1.0.10]
    at reactor.netty.channel.ChannelOperations.onInboundComplete(ChannelOperations.java:419) ~[com.mycompany.producerservice.ProducerServiceApplication:1.0.10]
    at reactor.netty.http.server.HttpServerOperations.onInboundNext(HttpServerOperations.java:547) ~[na:na]
    at reactor.netty.channel.ChannelOperationsHandler.channelRead(ChannelOperationsHandler.java:93) ~[na:na]
    at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:379) ~[na:na]
    at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:365) ~[na:na]
    at io.netty.channel.AbstractChannelHandlerContext.fireChannelRead(AbstractChannelHandlerContext.java:357) ~[na:na]
    at reactor.netty.http.server.HttpTrafficHandler.channelRead(HttpTrafficHandler.java:252) ~[na:na]
    at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:379) ~[na:na]
    at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:365) ~[na:na]
    at io.netty.channel.AbstractChannelHandlerContext.fireChannelRead(AbstractChannelHandlerContext.java:357) ~[na:na]
    at io.netty.channel.CombinedChannelDuplexHandler$DelegatingChannelHandlerContext.fireChannelRead(CombinedChannelDuplexHandler.java:436) ~[na:na]
    at io.netty.handler.codec.ByteToMessageDecoder.fireChannelRead(ByteToMessageDecoder.java:324) ~[na:na]
    at io.netty.handler.codec.ByteToMessageDecoder.channelRead(ByteToMessageDecoder.java:296) ~[na:na]
    at io.netty.channel.CombinedChannelDuplexHandler.channelRead(CombinedChannelDuplexHandler.java:251) ~[na:na]
    at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:379) ~[na:na]
    at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:365) ~[na:na]
    at io.netty.channel.AbstractChannelHandlerContext.fireChannelRead(AbstractChannelHandlerContext.java:357) ~[na:na]
    at io.netty.channel.DefaultChannelPipeline$HeadContext.channelRead(DefaultChannelPipeline.java:1410) ~[na:na]
    at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:379) ~[na:na]
    at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:365) ~[na:na]
    at io.netty.channel.DefaultChannelPipeline.fireChannelRead(DefaultChannelPipeline.java:919) ~[na:na]
    at io.netty.channel.nio.AbstractNioByteChannel$NioByteUnsafe.read(AbstractNioByteChannel.java:166) ~[na:na]
    at io.netty.channel.nio.NioEventLoop.processSelectedKey(NioEventLoop.java:719) ~[na:na]
    at io.netty.channel.nio.NioEventLoop.processSelectedKeysOptimized(NioEventLoop.java:655) ~[na:na]
    at io.netty.channel.nio.NioEventLoop.processSelectedKeys(NioEventLoop.java:581) ~[na:na]
    at io.netty.channel.nio.NioEventLoop.run(NioEventLoop.java:493) ~[na:na]
    at io.netty.util.concurrent.SingleThreadEventExecutor$4.run(SingleThreadEventExecutor.java:986) ~[na:na]
    at io.netty.util.internal.ThreadExecutorMap$2.run(ThreadExecutorMap.java:74) ~[na:na]
    at io.netty.util.concurrent.FastThreadLocalRunnable.run(FastThreadLocalRunnable.java:30) ~[na:na]
    at java.lang.Thread.run(Thread.java:829) ~[na:na]
    at com.oracle.svm.core.thread.JavaThreads.threadStartRoutine(JavaThreads.java:567) ~[na:na]
    at com.oracle.svm.core.posix.thread.PosixJavaThreads.pthreadStartRoutine(PosixJavaThreads.java:192) ~[na:na]
Caused by: com.fasterxml.jackson.databind.exc.InvalidDefinitionException: No serializer found for class org.springframework.validation.beanvalidation.SpringValidatorAdapter$ViolationFieldError and no properties discovered to create BeanSerializer (to avoid exception, disable SerializationFeature.FAIL_ON_EMPTY_BEANS) (through reference chain: java.util.LinkedHashMap["errors"]->java.util.Collections$UnmodifiableRandomAccessList[0])
    at com.fasterxml.jackson.databind.SerializerProvider.reportBadDefinition(SerializerProvider.java:1276) ~[na:na]
    at com.fasterxml.jackson.databind.DatabindContext.reportBadDefinition(DatabindContext.java:400) ~[na:na]
    at com.fasterxml.jackson.databind.ser.impl.UnknownSerializer.failForEmpty(UnknownSerializer.java:71) ~[na:na]
    at com.fasterxml.jackson.databind.ser.impl.UnknownSerializer.serialize(UnknownSerializer.java:33) ~[na:na]
    at com.fasterxml.jackson.databind.ser.impl.IndexedListSerializer.serializeContents(IndexedListSerializer.java:119) ~[na:na]
    at com.fasterxml.jackson.databind.ser.impl.IndexedListSerializer.serialize(IndexedListSerializer.java:79) ~[na:na]
    at com.fasterxml.jackson.databind.ser.impl.IndexedListSerializer.serialize(IndexedListSerializer.java:18) ~[na:na]
    at com.fasterxml.jackson.databind.ser.std.MapSerializer.serializeFields(MapSerializer.java:808) ~[na:na]
    at com.fasterxml.jackson.databind.ser.std.MapSerializer.serializeWithoutTypeInfo(MapSerializer.java:764) ~[na:na]
    at com.fasterxml.jackson.databind.ser.std.MapSerializer.serialize(MapSerializer.java:720) ~[na:na]
    at com.fasterxml.jackson.databind.ser.std.MapSerializer.serialize(MapSerializer.java:35) ~[na:na]
    at com.fasterxml.jackson.databind.ser.DefaultSerializerProvider._serialize(DefaultSerializerProvider.java:480) ~[na:na]
    at com.fasterxml.jackson.databind.ser.DefaultSerializerProvider.serializeValue(DefaultSerializerProvider.java:400) ~[na:na]
    at com.fasterxml.jackson.databind.ObjectWriter$Prefetch.serialize(ObjectWriter.java:1510) ~[na:na]
    at com.fasterxml.jackson.databind.ObjectWriter.writeValue(ObjectWriter.java:1006) ~[na:na]
    at org.springframework.http.codec.json.AbstractJackson2Encoder.encodeValue(AbstractJackson2Encoder.java:222) ~[na:na]
    ... 99 common frames omitted
```
