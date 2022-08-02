# spring-cloud-stream-event-routing-cloudevents

The goal of this project is to play with [`Spring Cloud Stream Event Routing`](https://docs.spring.io/spring-cloud-stream/docs/current/reference/html/spring-cloud-stream.html#_event_routing) and [`CloudEvents`](https://cloudevents.io/). For it, we will implement a producer and consumer of `news` & `alert` events.

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
- [`Docker-Compose`](https://docs.docker.com/compose/install/)

## Start Environment

- Open a terminal and inside `spring-cloud-stream-event-routing-cloudevents` root folder run
  ```
  docker-compose up -d
  ```

- Wait for Docker containers to be up and running. To check it, run
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
- To stop and remove docker-compose containers, network and volumes, go to a terminal and, inside `spring-cloud-stream-event-routing-cloudevents` root folder, run the following command
  ```
  docker-compose down -v
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

- Unable to run `producer-service` and `consumer-service` tests as **Mockito** is still not supported in AOT. See `spring-native` issues [#1343](https://github.com/spring-projects-experimental/spring-native/issues/1343) and [#1063](https://github.com/spring-projects-experimental/spring-native/issues/1063)

- `producer-service` Docker native image is built successfully. However, the following exception is thrown when a `news` or an `alert` is submitted
  ```
  ERROR 1 --- [ctor-http-nio-2] a.w.r.e.AbstractErrorWebExceptionHandler : [b7380552-1]  500 Server Error for HTTP POST "/api/news/rai"
  
  java.lang.IllegalArgumentException: Could not find class [org.springframework.boot.autoconfigure.condition.OnBeanCondition]
  	at org.springframework.util.ClassUtils.resolveClassName(ClassUtils.java:334) ~[na:na]
  	Suppressed: reactor.core.publisher.FluxOnAssembly$OnAssemblyException:
  Error has been observed at the following site(s):
  	*__checkpoint ? org.springframework.boot.actuate.metrics.web.reactive.server.MetricsWebFilter [DefaultWebFilterChain]
  	*__checkpoint ? HTTP POST "/api/news/rai" [ExceptionHandlingWebHandler]
  Original Stack Trace:
  		at org.springframework.util.ClassUtils.resolveClassName(ClassUtils.java:334) ~[na:na]
  		at org.springframework.context.annotation.ConditionEvaluator.getCondition(ConditionEvaluator.java:124) ~[na:na]
  		at org.springframework.context.annotation.ConditionEvaluator.shouldSkip(ConditionEvaluator.java:96) ~[na:na]
  		at org.springframework.context.annotation.ConditionEvaluator.shouldSkip(ConditionEvaluator.java:88) ~[na:na]
  		at org.springframework.context.annotation.ConditionEvaluator.shouldSkip(ConditionEvaluator.java:71) ~[na:na]
  		at org.springframework.context.annotation.AnnotatedBeanDefinitionReader.doRegisterBean(AnnotatedBeanDefinitionReader.java:254) ~[na:na]
  		at org.springframework.context.annotation.AnnotatedBeanDefinitionReader.registerBean(AnnotatedBeanDefinitionReader.java:147) ~[na:na]
  		at org.springframework.context.annotation.AnnotatedBeanDefinitionReader.register(AnnotatedBeanDefinitionReader.java:137) ~[na:na]
  		at org.springframework.context.annotation.AnnotationConfigApplicationContext.register(AnnotationConfigApplicationContext.java:168) ~[na:na]
  		at org.springframework.cloud.stream.binder.DefaultBinderFactory.initializeBinderContextSimple(DefaultBinderFactory.java:410) ~[na:na]
  		at org.springframework.cloud.stream.binder.DefaultBinderFactory.getBinderInstance(DefaultBinderFactory.java:265) ~[na:na]
  		at org.springframework.cloud.stream.binder.DefaultBinderFactory.doGetBinder(DefaultBinderFactory.java:223) ~[na:na]
  		at org.springframework.cloud.stream.binder.DefaultBinderFactory.getBinder(DefaultBinderFactory.java:151) ~[na:na]
  		at org.springframework.cloud.stream.binding.BindingService.getBinder(BindingService.java:394) ~[com.ivanfranchin.producerservice.ProducerServiceApplication:na]
  		at org.springframework.cloud.stream.binding.BindingService.bindProducer(BindingService.java:277) ~[com.ivanfranchin.producerservice.ProducerServiceApplication:na]
  		at org.springframework.cloud.stream.function.StreamBridge.resolveDestination(StreamBridge.java:296) ~[com.ivanfranchin.producerservice.ProducerServiceApplication:na]
  		at org.springframework.cloud.stream.function.StreamBridge.send(StreamBridge.java:213) ~[com.ivanfranchin.producerservice.ProducerServiceApplication:na]
  		at org.springframework.cloud.stream.function.StreamBridge.send(StreamBridge.java:170) ~[com.ivanfranchin.producerservice.ProducerServiceApplication:na]
  		at org.springframework.cloud.stream.function.StreamBridge.send(StreamBridge.java:150) ~[com.ivanfranchin.producerservice.ProducerServiceApplication:na]
  		at com.ivanfranchin.producerservice.kafka.news.NewsEventProducer.send(NewsEventProducer.java:31) ~[com.ivanfranchin.producerservice.ProducerServiceApplication:na]
  		at com.ivanfranchin.producerservice.rest.news.NewsController.createRAINews(NewsController.java:51) ~[com.ivanfranchin.producerservice.ProducerServiceApplication:na]
  		at java.lang.reflect.Method.invoke(Method.java:568) ~[com.ivanfranchin.producerservice.ProducerServiceApplication:na]
  		at org.springframework.web.reactive.result.method.InvocableHandlerMethod.lambda$invoke$0(InvocableHandlerMethod.java:144) ~[na:na]
  		at reactor.core.publisher.MonoFlatMap$FlatMapMain.onNext(MonoFlatMap.java:125) ~[com.ivanfranchin.producerservice.ProducerServiceApplication:3.4.21]
  		at reactor.core.publisher.Operators$MonoSubscriber.complete(Operators.java:1816) ~[com.ivanfranchin.producerservice.ProducerServiceApplication:3.4.21]
  		at reactor.core.publisher.MonoZip$ZipCoordinator.signal(MonoZip.java:251) ~[com.ivanfranchin.producerservice.ProducerServiceApplication:3.4.21]
  		at reactor.core.publisher.MonoZip$ZipInner.onNext(MonoZip.java:336) ~[com.ivanfranchin.producerservice.ProducerServiceApplication:3.4.21]
  		at reactor.core.publisher.MonoPeekTerminal$MonoTerminalPeekSubscriber.onNext(MonoPeekTerminal.java:180) ~[na:na]
  		at reactor.core.publisher.FluxDefaultIfEmpty$DefaultIfEmptySubscriber.onNext(FluxDefaultIfEmpty.java:101) ~[na:na]
  		at reactor.core.publisher.FluxPeek$PeekSubscriber.onNext(FluxPeek.java:200) ~[na:na]
  		at reactor.core.publisher.FluxSwitchIfEmpty$SwitchIfEmptySubscriber.onNext(FluxSwitchIfEmpty.java:74) ~[na:na]
  		at reactor.core.publisher.FluxOnErrorResume$ResumeSubscriber.onNext(FluxOnErrorResume.java:79) ~[na:na]
  		at reactor.core.publisher.Operators$MonoSubscriber.complete(Operators.java:1816) ~[com.ivanfranchin.producerservice.ProducerServiceApplication:3.4.21]
  		at reactor.core.publisher.MonoFlatMap$FlatMapMain.onNext(MonoFlatMap.java:151) ~[com.ivanfranchin.producerservice.ProducerServiceApplication:3.4.21]
  		at reactor.core.publisher.FluxContextWrite$ContextWriteSubscriber.onNext(FluxContextWrite.java:107) ~[na:na]
  		at reactor.core.publisher.FluxMapFuseable$MapFuseableConditionalSubscriber.onNext(FluxMapFuseable.java:299) ~[na:na]
  		at reactor.core.publisher.FluxFilterFuseable$FilterFuseableConditionalSubscriber.onNext(FluxFilterFuseable.java:337) ~[na:na]
  		at reactor.core.publisher.Operators$MonoSubscriber.complete(Operators.java:1816) ~[com.ivanfranchin.producerservice.ProducerServiceApplication:3.4.21]
  		at reactor.core.publisher.MonoCollect$CollectSubscriber.onComplete(MonoCollect.java:160) ~[na:na]
  		at reactor.core.publisher.FluxMap$MapSubscriber.onComplete(FluxMap.java:144) ~[na:na]
  		at reactor.core.publisher.FluxPeek$PeekSubscriber.onComplete(FluxPeek.java:260) ~[na:na]
  		at reactor.core.publisher.FluxMap$MapSubscriber.onComplete(FluxMap.java:144) ~[na:na]
  		at reactor.netty.channel.FluxReceive.onInboundComplete(FluxReceive.java:400) ~[com.ivanfranchin.producerservice.ProducerServiceApplication:1.0.21]
  		at reactor.netty.channel.ChannelOperations.onInboundComplete(ChannelOperations.java:419) ~[com.ivanfranchin.producerservice.ProducerServiceApplication:1.0.21]
  		at reactor.netty.http.server.HttpServerOperations.onInboundNext(HttpServerOperations.java:600) ~[com.ivanfranchin.producerservice.ProducerServiceApplication:1.0.21]
  		at reactor.netty.channel.ChannelOperationsHandler.channelRead(ChannelOperationsHandler.java:93) ~[na:na]
  		at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:379) ~[com.ivanfranchin.producerservice.ProducerServiceApplication:4.1.79.Final]
  		at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:365) ~[com.ivanfranchin.producerservice.ProducerServiceApplication:4.1.79.Final]
  		at io.netty.channel.AbstractChannelHandlerContext.fireChannelRead(AbstractChannelHandlerContext.java:357) ~[com.ivanfranchin.producerservice.ProducerServiceApplication:4.1.79.Final]
  		at reactor.netty.http.server.HttpTrafficHandler.channelRead(HttpTrafficHandler.java:266) ~[na:na]
  		at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:379) ~[com.ivanfranchin.producerservice.ProducerServiceApplication:4.1.79.Final]
  		at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:365) ~[com.ivanfranchin.producerservice.ProducerServiceApplication:4.1.79.Final]
  		at io.netty.channel.AbstractChannelHandlerContext.fireChannelRead(AbstractChannelHandlerContext.java:357) ~[com.ivanfranchin.producerservice.ProducerServiceApplication:4.1.79.Final]
  		at io.netty.channel.CombinedChannelDuplexHandler$DelegatingChannelHandlerContext.fireChannelRead(CombinedChannelDuplexHandler.java:436) ~[com.ivanfranchin.producerservice.ProducerServiceApplication:4.1.79.Final]
  		at io.netty.handler.codec.ByteToMessageDecoder.fireChannelRead(ByteToMessageDecoder.java:327) ~[com.ivanfranchin.producerservice.ProducerServiceApplication:4.1.79.Final]
  		at io.netty.handler.codec.ByteToMessageDecoder.channelRead(ByteToMessageDecoder.java:299) ~[com.ivanfranchin.producerservice.ProducerServiceApplication:4.1.79.Final]
  		at io.netty.channel.CombinedChannelDuplexHandler.channelRead(CombinedChannelDuplexHandler.java:251) ~[com.ivanfranchin.producerservice.ProducerServiceApplication:4.1.79.Final]
  		at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:379) ~[com.ivanfranchin.producerservice.ProducerServiceApplication:4.1.79.Final]
  		at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:365) ~[com.ivanfranchin.producerservice.ProducerServiceApplication:4.1.79.Final]
  		at io.netty.channel.AbstractChannelHandlerContext.fireChannelRead(AbstractChannelHandlerContext.java:357) ~[com.ivanfranchin.producerservice.ProducerServiceApplication:4.1.79.Final]
  		at io.netty.channel.DefaultChannelPipeline$HeadContext.channelRead(DefaultChannelPipeline.java:1410) ~[na:na]
  		at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:379) ~[com.ivanfranchin.producerservice.ProducerServiceApplication:4.1.79.Final]
  		at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:365) ~[com.ivanfranchin.producerservice.ProducerServiceApplication:4.1.79.Final]
  		at io.netty.channel.DefaultChannelPipeline.fireChannelRead(DefaultChannelPipeline.java:919) ~[com.ivanfranchin.producerservice.ProducerServiceApplication:4.1.79.Final]
  		at io.netty.channel.nio.AbstractNioByteChannel$NioByteUnsafe.read(AbstractNioByteChannel.java:166) ~[com.ivanfranchin.producerservice.ProducerServiceApplication:4.1.79.Final]
  		at io.netty.channel.nio.NioEventLoop.processSelectedKey(NioEventLoop.java:722) ~[na:na]
  		at io.netty.channel.nio.NioEventLoop.processSelectedKeysOptimized(NioEventLoop.java:658) ~[na:na]
  		at io.netty.channel.nio.NioEventLoop.processSelectedKeys(NioEventLoop.java:584) ~[na:na]
  		at io.netty.channel.nio.NioEventLoop.run(NioEventLoop.java:496) ~[na:na]
  		at io.netty.util.concurrent.SingleThreadEventExecutor$4.run(SingleThreadEventExecutor.java:997) ~[na:na]
  		at io.netty.util.internal.ThreadExecutorMap$2.run(ThreadExecutorMap.java:74) ~[na:na]
  		at io.netty.util.concurrent.FastThreadLocalRunnable.run(FastThreadLocalRunnable.java:30) ~[na:na]
  		at java.lang.Thread.run(Thread.java:833) ~[com.ivanfranchin.producerservice.ProducerServiceApplication:na]
  		at com.oracle.svm.core.thread.PlatformThreads.threadStartRoutine(PlatformThreads.java:704) ~[com.ivanfranchin.producerservice.ProducerServiceApplication:na]
  		at com.oracle.svm.core.posix.thread.PosixPlatformThreads.pthreadStartRoutine(PosixPlatformThreads.java:202) ~[na:na]
  Caused by: java.lang.ClassNotFoundException: org.springframework.boot.autoconfigure.condition.OnBeanCondition
  	at java.lang.Class.forName(DynamicHub.java:1121) ~[com.ivanfranchin.producerservice.ProducerServiceApplication:na]
  	at org.springframework.util.ClassUtils.forName(ClassUtils.java:284) ~[na:na]
  	at org.springframework.util.ClassUtils.resolveClassName(ClassUtils.java:324) ~[na:na]
  	at org.springframework.context.annotation.ConditionEvaluator.getCondition(ConditionEvaluator.java:124) ~[na:na]
  	at org.springframework.context.annotation.ConditionEvaluator.shouldSkip(ConditionEvaluator.java:96) ~[na:na]
  	at org.springframework.context.annotation.ConditionEvaluator.shouldSkip(ConditionEvaluator.java:88) ~[na:na]
  	at org.springframework.context.annotation.ConditionEvaluator.shouldSkip(ConditionEvaluator.java:71) ~[na:na]
  	at org.springframework.context.annotation.AnnotatedBeanDefinitionReader.doRegisterBean(AnnotatedBeanDefinitionReader.java:254) ~[na:na]
  	at org.springframework.context.annotation.AnnotatedBeanDefinitionReader.registerBean(AnnotatedBeanDefinitionReader.java:147) ~[na:na]
  	at org.springframework.context.annotation.AnnotatedBeanDefinitionReader.register(AnnotatedBeanDefinitionReader.java:137) ~[na:na]
  	at org.springframework.context.annotation.AnnotationConfigApplicationContext.register(AnnotationConfigApplicationContext.java:168) ~[na:na]
  	at org.springframework.cloud.stream.binder.DefaultBinderFactory.initializeBinderContextSimple(DefaultBinderFactory.java:410) ~[na:na]
  	at org.springframework.cloud.stream.binder.DefaultBinderFactory.getBinderInstance(DefaultBinderFactory.java:265) ~[na:na]
  	at org.springframework.cloud.stream.binder.DefaultBinderFactory.doGetBinder(DefaultBinderFactory.java:223) ~[na:na]
  	at org.springframework.cloud.stream.binder.DefaultBinderFactory.getBinder(DefaultBinderFactory.java:151) ~[na:na]
  	at org.springframework.cloud.stream.binding.BindingService.getBinder(BindingService.java:394) ~[com.ivanfranchin.producerservice.ProducerServiceApplication:na]
  	at org.springframework.cloud.stream.binding.BindingService.bindProducer(BindingService.java:277) ~[com.ivanfranchin.producerservice.ProducerServiceApplication:na]
  	at org.springframework.cloud.stream.function.StreamBridge.resolveDestination(StreamBridge.java:296) ~[com.ivanfranchin.producerservice.ProducerServiceApplication:na]
  	at org.springframework.cloud.stream.function.StreamBridge.send(StreamBridge.java:213) ~[com.ivanfranchin.producerservice.ProducerServiceApplication:na]
  	at org.springframework.cloud.stream.function.StreamBridge.send(StreamBridge.java:170) ~[com.ivanfranchin.producerservice.ProducerServiceApplication:na]
  	at org.springframework.cloud.stream.function.StreamBridge.send(StreamBridge.java:150) ~[com.ivanfranchin.producerservice.ProducerServiceApplication:na]
  	at com.ivanfranchin.producerservice.kafka.news.NewsEventProducer.send(NewsEventProducer.java:31) ~[com.ivanfranchin.producerservice.ProducerServiceApplication:na]
  	at com.ivanfranchin.producerservice.rest.news.NewsController.createRAINews(NewsController.java:51) ~[com.ivanfranchin.producerservice.ProducerServiceApplication:na]
  	at java.lang.reflect.Method.invoke(Method.java:568) ~[com.ivanfranchin.producerservice.ProducerServiceApplication:na]
  	at org.springframework.web.reactive.result.method.InvocableHandlerMethod.lambda$invoke$0(InvocableHandlerMethod.java:144) ~[na:na]
  	at reactor.core.publisher.MonoFlatMap$FlatMapMain.onNext(MonoFlatMap.java:125) ~[com.ivanfranchin.producerservice.ProducerServiceApplication:3.4.21]
  	at reactor.core.publisher.Operators$MonoSubscriber.complete(Operators.java:1816) ~[com.ivanfranchin.producerservice.ProducerServiceApplication:3.4.21]
  	at reactor.core.publisher.MonoZip$ZipCoordinator.signal(MonoZip.java:251) ~[com.ivanfranchin.producerservice.ProducerServiceApplication:3.4.21]
  	at reactor.core.publisher.MonoZip$ZipInner.onNext(MonoZip.java:336) ~[com.ivanfranchin.producerservice.ProducerServiceApplication:3.4.21]
  	at reactor.core.publisher.MonoPeekTerminal$MonoTerminalPeekSubscriber.onNext(MonoPeekTerminal.java:180) ~[na:na]
  	at reactor.core.publisher.FluxDefaultIfEmpty$DefaultIfEmptySubscriber.onNext(FluxDefaultIfEmpty.java:101) ~[na:na]
  	at reactor.core.publisher.FluxPeek$PeekSubscriber.onNext(FluxPeek.java:200) ~[na:na]
  	at reactor.core.publisher.FluxSwitchIfEmpty$SwitchIfEmptySubscriber.onNext(FluxSwitchIfEmpty.java:74) ~[na:na]
  	at reactor.core.publisher.FluxOnErrorResume$ResumeSubscriber.onNext(FluxOnErrorResume.java:79) ~[na:na]
  	at reactor.core.publisher.Operators$MonoSubscriber.complete(Operators.java:1816) ~[com.ivanfranchin.producerservice.ProducerServiceApplication:3.4.21]
  	at reactor.core.publisher.MonoFlatMap$FlatMapMain.onNext(MonoFlatMap.java:151) ~[com.ivanfranchin.producerservice.ProducerServiceApplication:3.4.21]
  	at reactor.core.publisher.FluxContextWrite$ContextWriteSubscriber.onNext(FluxContextWrite.java:107) ~[na:na]
  	at reactor.core.publisher.FluxMapFuseable$MapFuseableConditionalSubscriber.onNext(FluxMapFuseable.java:299) ~[na:na]
  	at reactor.core.publisher.FluxFilterFuseable$FilterFuseableConditionalSubscriber.onNext(FluxFilterFuseable.java:337) ~[na:na]
  	at reactor.core.publisher.Operators$MonoSubscriber.complete(Operators.java:1816) ~[com.ivanfranchin.producerservice.ProducerServiceApplication:3.4.21]
  	at reactor.core.publisher.MonoCollect$CollectSubscriber.onComplete(MonoCollect.java:160) ~[na:na]
  	at reactor.core.publisher.FluxMap$MapSubscriber.onComplete(FluxMap.java:144) ~[na:na]
  	at reactor.core.publisher.FluxPeek$PeekSubscriber.onComplete(FluxPeek.java:260) ~[na:na]
  	at reactor.core.publisher.FluxMap$MapSubscriber.onComplete(FluxMap.java:144) ~[na:na]
  	at reactor.netty.channel.FluxReceive.onInboundComplete(FluxReceive.java:400) ~[com.ivanfranchin.producerservice.ProducerServiceApplication:1.0.21]
  	at reactor.netty.channel.ChannelOperations.onInboundComplete(ChannelOperations.java:419) ~[com.ivanfranchin.producerservice.ProducerServiceApplication:1.0.21]
  	at reactor.netty.http.server.HttpServerOperations.onInboundNext(HttpServerOperations.java:600) ~[com.ivanfranchin.producerservice.ProducerServiceApplication:1.0.21]
  	at reactor.netty.channel.ChannelOperationsHandler.channelRead(ChannelOperationsHandler.java:93) ~[na:na]
  	at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:379) ~[com.ivanfranchin.producerservice.ProducerServiceApplication:4.1.79.Final]
  	at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:365) ~[com.ivanfranchin.producerservice.ProducerServiceApplication:4.1.79.Final]
  	at io.netty.channel.AbstractChannelHandlerContext.fireChannelRead(AbstractChannelHandlerContext.java:357) ~[com.ivanfranchin.producerservice.ProducerServiceApplication:4.1.79.Final]
  	at reactor.netty.http.server.HttpTrafficHandler.channelRead(HttpTrafficHandler.java:266) ~[na:na]
  	at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:379) ~[com.ivanfranchin.producerservice.ProducerServiceApplication:4.1.79.Final]
  	at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:365) ~[com.ivanfranchin.producerservice.ProducerServiceApplication:4.1.79.Final]
  	at io.netty.channel.AbstractChannelHandlerContext.fireChannelRead(AbstractChannelHandlerContext.java:357) ~[com.ivanfranchin.producerservice.ProducerServiceApplication:4.1.79.Final]
  	at io.netty.channel.CombinedChannelDuplexHandler$DelegatingChannelHandlerContext.fireChannelRead(CombinedChannelDuplexHandler.java:436) ~[com.ivanfranchin.producerservice.ProducerServiceApplication:4.1.79.Final]
  	at io.netty.handler.codec.ByteToMessageDecoder.fireChannelRead(ByteToMessageDecoder.java:327) ~[com.ivanfranchin.producerservice.ProducerServiceApplication:4.1.79.Final]
  	at io.netty.handler.codec.ByteToMessageDecoder.channelRead(ByteToMessageDecoder.java:299) ~[com.ivanfranchin.producerservice.ProducerServiceApplication:4.1.79.Final]
  	at io.netty.channel.CombinedChannelDuplexHandler.channelRead(CombinedChannelDuplexHandler.java:251) ~[com.ivanfranchin.producerservice.ProducerServiceApplication:4.1.79.Final]
  	at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:379) ~[com.ivanfranchin.producerservice.ProducerServiceApplication:4.1.79.Final]
  	at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:365) ~[com.ivanfranchin.producerservice.ProducerServiceApplication:4.1.79.Final]
  	at io.netty.channel.AbstractChannelHandlerContext.fireChannelRead(AbstractChannelHandlerContext.java:357) ~[com.ivanfranchin.producerservice.ProducerServiceApplication:4.1.79.Final]
  	at io.netty.channel.DefaultChannelPipeline$HeadContext.channelRead(DefaultChannelPipeline.java:1410) ~[na:na]
  	at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:379) ~[com.ivanfranchin.producerservice.ProducerServiceApplication:4.1.79.Final]
  	at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:365) ~[com.ivanfranchin.producerservice.ProducerServiceApplication:4.1.79.Final]
  	at io.netty.channel.DefaultChannelPipeline.fireChannelRead(DefaultChannelPipeline.java:919) ~[com.ivanfranchin.producerservice.ProducerServiceApplication:4.1.79.Final]
  	at io.netty.channel.nio.AbstractNioByteChannel$NioByteUnsafe.read(AbstractNioByteChannel.java:166) ~[com.ivanfranchin.producerservice.ProducerServiceApplication:4.1.79.Final]
  	at io.netty.channel.nio.NioEventLoop.processSelectedKey(NioEventLoop.java:722) ~[na:na]
  	at io.netty.channel.nio.NioEventLoop.processSelectedKeysOptimized(NioEventLoop.java:658) ~[na:na]
  	at io.netty.channel.nio.NioEventLoop.processSelectedKeys(NioEventLoop.java:584) ~[na:na]
  	at io.netty.channel.nio.NioEventLoop.run(NioEventLoop.java:496) ~[na:na]
  	at io.netty.util.concurrent.SingleThreadEventExecutor$4.run(SingleThreadEventExecutor.java:997) ~[na:na]
  	at io.netty.util.internal.ThreadExecutorMap$2.run(ThreadExecutorMap.java:74) ~[na:na]
  	at io.netty.util.concurrent.FastThreadLocalRunnable.run(FastThreadLocalRunnable.java:30) ~[na:na]
  	at java.lang.Thread.run(Thread.java:833) ~[com.ivanfranchin.producerservice.ProducerServiceApplication:na]
  	at com.oracle.svm.core.thread.PlatformThreads.threadStartRoutine(PlatformThreads.java:704) ~[com.ivanfranchin.producerservice.ProducerServiceApplication:na]
  	at com.oracle.svm.core.posix.thread.PosixPlatformThreads.pthreadStartRoutine(PosixPlatformThreads.java:202) ~[na:na]
  ```

- `consumer-service` Docker native images is built successfully. However, the following exception is thrown at startup
  ```
  ERROR 1 --- [           main] o.s.b.d.LoggingFailureAnalysisReporter   :
  
  ***************************
  APPLICATION FAILED TO START
  ***************************
  
  Description:
  
  Native reflection configuration for org.springframework.boot.autoconfigure.condition.OnBeanCondition is missing.
  
  Action:
  
  Native configuration for a class accessed reflectively is likely missing.
  You can try to configure native hints in order to specify it explicitly.
  See https://docs.spring.io/spring-native/docs/current/reference/htmlsingle/#native-hints for more details.
  ```
