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
  POST /api/alerts/earthquake {"richterScale": "...", "epicenterLat": "...", "epicenterLon": "..."}
  POST /api/alerts/weather {"message": "..."}
  ```

- ### consumer-service

  `Spring Boot` application that consumes the `news` & `alert` events published by `producer-service`.

## Prerequisites

- [`Java 11+`](https://www.oracle.com/java/technologies/downloads/#java11)
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

> **Note:** [HTTPie](https://httpie.org/) is being used in the calls bellow

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
  ERROR 1 --- [ctor-http-nio-3] a.w.r.e.AbstractErrorWebExceptionHandler : [7f7c07d9-3]  500 Server Error for HTTP POST "/api/alerts/weather"
  
  java.lang.IllegalArgumentException: Attribute 'value' not found in attributes for annotation [org.springframework.context.annotation.Role]
  	at org.springframework.util.Assert.notNull(Assert.java:219) ~[na:na]
  	Suppressed: reactor.core.publisher.FluxOnAssembly$OnAssemblyException:
  Error has been observed at the following site(s):
  	*__checkpoint ? org.springframework.boot.actuate.metrics.web.reactive.server.MetricsWebFilter [DefaultWebFilterChain]
  	*__checkpoint ? HTTP POST "/api/alerts/weather" [ExceptionHandlingWebHandler]
  Original Stack Trace:
  		at org.springframework.util.Assert.notNull(Assert.java:219) ~[na:na]
  		at org.springframework.core.annotation.AnnotationAttributes.assertAttributePresence(AnnotationAttributes.java:366) ~[na:na]
  		at org.springframework.core.annotation.AnnotationAttributes.getRequiredAttribute(AnnotationAttributes.java:353) ~[na:na]
  		at org.springframework.core.annotation.AnnotationAttributes.getNumber(AnnotationAttributes.java:222) ~[na:na]
  		at org.springframework.context.annotation.AnnotationConfigUtils.processCommonDefinitionAnnotations(AnnotationConfigUtils.java:259) ~[na:na]
  		at org.springframework.context.annotation.AnnotationConfigUtils.processCommonDefinitionAnnotations(AnnotationConfigUtils.java:234) ~[na:na]
  		at org.springframework.context.annotation.AnnotatedBeanDefinitionReader.doRegisterBean(AnnotatedBeanDefinitionReader.java:263) ~[na:na]
  		at org.springframework.context.annotation.AnnotatedBeanDefinitionReader.registerBean(AnnotatedBeanDefinitionReader.java:147) ~[na:na]
  		at org.springframework.context.annotation.AnnotatedBeanDefinitionReader.register(AnnotatedBeanDefinitionReader.java:137) ~[na:na]
  		at org.springframework.context.annotation.AnnotationConfigApplicationContext.register(AnnotationConfigApplicationContext.java:168) ~[na:na]
  		at org.springframework.cloud.stream.binder.DefaultBinderFactory.initializeBinderContextSimple(DefaultBinderFactory.java:410) ~[na:na]
  		at org.springframework.cloud.stream.binder.DefaultBinderFactory.getBinderInstance(DefaultBinderFactory.java:265) ~[na:na]
  		at org.springframework.cloud.stream.binder.DefaultBinderFactory.doGetBinder(DefaultBinderFactory.java:223) ~[na:na]
  		at org.springframework.cloud.stream.binder.DefaultBinderFactory.getBinder(DefaultBinderFactory.java:151) ~[na:na]
  		at org.springframework.cloud.stream.binding.BindingService.getBinder(BindingService.java:389) ~[na:na]
  		at org.springframework.cloud.stream.binding.BindingService.bindProducer(BindingService.java:273) ~[na:na]
  		at org.springframework.cloud.stream.function.StreamBridge.resolveDestination(StreamBridge.java:282) ~[na:na]
  		at org.springframework.cloud.stream.function.StreamBridge.send(StreamBridge.java:210) ~[na:na]
  		at org.springframework.cloud.stream.function.StreamBridge.send(StreamBridge.java:164) ~[na:na]
  		at org.springframework.cloud.stream.function.StreamBridge.send(StreamBridge.java:144) ~[na:na]
  		at com.mycompany.producerservice.kafka.alert.AlertEventProducer.send(AlertEventProducer.java:27) ~[com.mycompany.producerservice.ProducerServiceApplication:na]
  		at com.mycompany.producerservice.rest.alert.AlertController.createWeatherAlert(AlertController.java:40) ~[com.mycompany.producerservice.ProducerServiceApplication:na]
  		at java.lang.reflect.Method.invoke(Method.java:566) ~[na:na]
  		at org.springframework.web.reactive.result.method.InvocableHandlerMethod.lambda$invoke$0(InvocableHandlerMethod.java:144) ~[na:na]
  		at reactor.core.publisher.MonoFlatMap$FlatMapMain.onNext(MonoFlatMap.java:125) ~[com.mycompany.producerservice.ProducerServiceApplication:3.4.15]
  		at reactor.core.publisher.Operators$MonoSubscriber.complete(Operators.java:1816) ~[com.mycompany.producerservice.ProducerServiceApplication:3.4.15]
  		at reactor.core.publisher.MonoZip$ZipCoordinator.signal(MonoZip.java:251) ~[com.mycompany.producerservice.ProducerServiceApplication:3.4.15]
  		at reactor.core.publisher.MonoZip$ZipInner.onNext(MonoZip.java:336) ~[com.mycompany.producerservice.ProducerServiceApplication:3.4.15]
  		at reactor.core.publisher.MonoPeekTerminal$MonoTerminalPeekSubscriber.onNext(MonoPeekTerminal.java:180) ~[na:na]
  		at reactor.core.publisher.FluxDefaultIfEmpty$DefaultIfEmptySubscriber.onNext(FluxDefaultIfEmpty.java:101) ~[na:na]
  		at reactor.core.publisher.FluxPeek$PeekSubscriber.onNext(FluxPeek.java:200) ~[na:na]
  		at reactor.core.publisher.FluxSwitchIfEmpty$SwitchIfEmptySubscriber.onNext(FluxSwitchIfEmpty.java:74) ~[na:na]
  		at reactor.core.publisher.FluxOnErrorResume$ResumeSubscriber.onNext(FluxOnErrorResume.java:79) ~[na:na]
  		at reactor.core.publisher.Operators$MonoSubscriber.complete(Operators.java:1816) ~[com.mycompany.producerservice.ProducerServiceApplication:3.4.15]
  		at reactor.core.publisher.MonoFlatMap$FlatMapMain.onNext(MonoFlatMap.java:151) ~[com.mycompany.producerservice.ProducerServiceApplication:3.4.15]
  		at reactor.core.publisher.FluxContextWrite$ContextWriteSubscriber.onNext(FluxContextWrite.java:107) ~[na:na]
  		at reactor.core.publisher.FluxMapFuseable$MapFuseableConditionalSubscriber.onNext(FluxMapFuseable.java:295) ~[na:na]
  		at reactor.core.publisher.FluxFilterFuseable$FilterFuseableConditionalSubscriber.onNext(FluxFilterFuseable.java:337) ~[na:na]
  		at reactor.core.publisher.Operators$MonoSubscriber.complete(Operators.java:1816) ~[com.mycompany.producerservice.ProducerServiceApplication:3.4.15]
  		at reactor.core.publisher.MonoCollect$CollectSubscriber.onComplete(MonoCollect.java:159) ~[na:na]
  		at reactor.core.publisher.FluxMap$MapSubscriber.onComplete(FluxMap.java:142) ~[na:na]
  		at reactor.core.publisher.FluxPeek$PeekSubscriber.onComplete(FluxPeek.java:260) ~[na:na]
  		at reactor.core.publisher.FluxMap$MapSubscriber.onComplete(FluxMap.java:142) ~[na:na]
  		at reactor.netty.channel.FluxReceive.onInboundComplete(FluxReceive.java:400) ~[com.mycompany.producerservice.ProducerServiceApplication:1.0.16]
  		at reactor.netty.channel.ChannelOperations.onInboundComplete(ChannelOperations.java:419) ~[com.mycompany.producerservice.ProducerServiceApplication:1.0.16]
  		at reactor.netty.http.server.HttpServerOperations.onInboundNext(HttpServerOperations.java:600) ~[na:na]
  		at reactor.netty.channel.ChannelOperationsHandler.channelRead(ChannelOperationsHandler.java:93) ~[na:na]
  		at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:379) ~[na:na]
  		at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:365) ~[na:na]
  		at io.netty.channel.AbstractChannelHandlerContext.fireChannelRead(AbstractChannelHandlerContext.java:357) ~[na:na]
  		at reactor.netty.http.server.HttpTrafficHandler.channelRead(HttpTrafficHandler.java:264) ~[na:na]
  		at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:379) ~[na:na]
  		at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:365) ~[na:na]
  		at io.netty.channel.AbstractChannelHandlerContext.fireChannelRead(AbstractChannelHandlerContext.java:357) ~[na:na]
  		at io.netty.channel.CombinedChannelDuplexHandler$DelegatingChannelHandlerContext.fireChannelRead(CombinedChannelDuplexHandler.java:436) ~[na:na]
  		at io.netty.handler.codec.ByteToMessageDecoder.fireChannelRead(ByteToMessageDecoder.java:327) ~[na:na]
  		at io.netty.handler.codec.ByteToMessageDecoder.channelRead(ByteToMessageDecoder.java:299) ~[na:na]
  		at io.netty.channel.CombinedChannelDuplexHandler.channelRead(CombinedChannelDuplexHandler.java:251) ~[na:na]
  		at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:379) ~[na:na]
  		at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:365) ~[na:na]
  		at io.netty.channel.AbstractChannelHandlerContext.fireChannelRead(AbstractChannelHandlerContext.java:357) ~[na:na]
  		at io.netty.channel.DefaultChannelPipeline$HeadContext.channelRead(DefaultChannelPipeline.java:1410) ~[na:na]
  		at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:379) ~[na:na]
  		at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:365) ~[na:na]
  		at io.netty.channel.DefaultChannelPipeline.fireChannelRead(DefaultChannelPipeline.java:919) ~[na:na]
  		at io.netty.channel.nio.AbstractNioByteChannel$NioByteUnsafe.read(AbstractNioByteChannel.java:166) ~[na:na]
  		at io.netty.channel.nio.NioEventLoop.processSelectedKey(NioEventLoop.java:722) ~[na:na]
  		at io.netty.channel.nio.NioEventLoop.processSelectedKeysOptimized(NioEventLoop.java:658) ~[na:na]
  		at io.netty.channel.nio.NioEventLoop.processSelectedKeys(NioEventLoop.java:584) ~[na:na]
  		at io.netty.channel.nio.NioEventLoop.run(NioEventLoop.java:496) ~[na:na]
  		at io.netty.util.concurrent.SingleThreadEventExecutor$4.run(SingleThreadEventExecutor.java:986) ~[na:na]
  		at io.netty.util.internal.ThreadExecutorMap$2.run(ThreadExecutorMap.java:74) ~[na:na]
  		at io.netty.util.concurrent.FastThreadLocalRunnable.run(FastThreadLocalRunnable.java:30) ~[na:na]
  		at java.lang.Thread.run(Thread.java:829) ~[na:na]
  		at com.oracle.svm.core.thread.JavaThreads.threadStartRoutine(JavaThreads.java:597) ~[na:na]
  		at com.oracle.svm.core.posix.thread.PosixJavaThreads.pthreadStartRoutine(PosixJavaThreads.java:194) ~[na:na]
  ```

- `consumer-service` Docker native images is built successfully. However, the following exception is thrown at startup
  ```
  ERROR 1 --- [           main] o.s.boot.SpringApplication               : Application run failed
  
  org.springframework.context.ApplicationContextException: Failed to start bean 'inputBindingLifecycle'; nested exception is java.lang.IllegalArgumentException: Attribute 'value' not found in attributes for annotation [org.springframework.context.annotation.Role]
  	at org.springframework.context.support.DefaultLifecycleProcessor.doStart(DefaultLifecycleProcessor.java:181) ~[na:na]
  	at org.springframework.context.support.DefaultLifecycleProcessor.access$200(DefaultLifecycleProcessor.java:54) ~[na:na]
  	at org.springframework.context.support.DefaultLifecycleProcessor$LifecycleGroup.start(DefaultLifecycleProcessor.java:356) ~[na:na]
  	at java.lang.Iterable.forEach(Iterable.java:75) ~[na:na]
  	at org.springframework.context.support.DefaultLifecycleProcessor.startBeans(DefaultLifecycleProcessor.java:155) ~[na:na]
  	at org.springframework.context.support.DefaultLifecycleProcessor.onRefresh(DefaultLifecycleProcessor.java:123) ~[na:na]
  	at org.springframework.context.support.AbstractApplicationContext.finishRefresh(AbstractApplicationContext.java:935) ~[na:na]
  	at org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:586) ~[na:na]
  	at org.springframework.boot.web.reactive.context.ReactiveWebServerApplicationContext.refresh(ReactiveWebServerApplicationContext.java:64) ~[na:na]
  	at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:740) ~[com.mycompany.consumerservice.ConsumerServiceApplication:2.6.4]
  	at org.springframework.boot.SpringApplication.refreshContext(SpringApplication.java:415) ~[com.mycompany.consumerservice.ConsumerServiceApplication:2.6.4]
  	at org.springframework.boot.SpringApplication.run(SpringApplication.java:303) ~[com.mycompany.consumerservice.ConsumerServiceApplication:2.6.4]
  	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1312) ~[com.mycompany.consumerservice.ConsumerServiceApplication:2.6.4]
  	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1301) ~[com.mycompany.consumerservice.ConsumerServiceApplication:2.6.4]
  	at com.mycompany.consumerservice.ConsumerServiceApplication.main(ConsumerServiceApplication.java:27) ~[com.mycompany.consumerservice.ConsumerServiceApplication:na]
  Caused by: java.lang.IllegalArgumentException: Attribute 'value' not found in attributes for annotation [org.springframework.context.annotation.Role]
  	at org.springframework.util.Assert.notNull(Assert.java:219) ~[na:na]
  	at org.springframework.core.annotation.AnnotationAttributes.assertAttributePresence(AnnotationAttributes.java:366) ~[na:na]
  	at org.springframework.core.annotation.AnnotationAttributes.getRequiredAttribute(AnnotationAttributes.java:353) ~[na:na]
  	at org.springframework.core.annotation.AnnotationAttributes.getNumber(AnnotationAttributes.java:222) ~[na:na]
  	at org.springframework.context.annotation.AnnotationConfigUtils.processCommonDefinitionAnnotations(AnnotationConfigUtils.java:259) ~[na:na]
  	at org.springframework.context.annotation.AnnotationConfigUtils.processCommonDefinitionAnnotations(AnnotationConfigUtils.java:234) ~[na:na]
  	at org.springframework.context.annotation.AnnotatedBeanDefinitionReader.doRegisterBean(AnnotatedBeanDefinitionReader.java:263) ~[na:na]
  	at org.springframework.context.annotation.AnnotatedBeanDefinitionReader.registerBean(AnnotatedBeanDefinitionReader.java:147) ~[na:na]
  	at org.springframework.context.annotation.AnnotatedBeanDefinitionReader.register(AnnotatedBeanDefinitionReader.java:137) ~[na:na]
  	at org.springframework.context.annotation.AnnotationConfigApplicationContext.register(AnnotationConfigApplicationContext.java:168) ~[na:na]
  	at org.springframework.cloud.stream.binder.DefaultBinderFactory.initializeBinderContextSimple(DefaultBinderFactory.java:410) ~[na:na]
  	at org.springframework.cloud.stream.binder.DefaultBinderFactory.getBinderInstance(DefaultBinderFactory.java:265) ~[na:na]
  	at org.springframework.cloud.stream.binder.DefaultBinderFactory.doGetBinder(DefaultBinderFactory.java:223) ~[na:na]
  	at org.springframework.cloud.stream.binder.DefaultBinderFactory.getBinder(DefaultBinderFactory.java:151) ~[na:na]
  	at org.springframework.cloud.stream.binding.BindingService.getBinder(BindingService.java:389) ~[na:na]
  	at org.springframework.cloud.stream.binding.BindingService.bindConsumer(BindingService.java:106) ~[na:na]
  	at org.springframework.cloud.stream.binding.AbstractBindableProxyFactory.createAndBindInputs(AbstractBindableProxyFactory.java:118) ~[na:na]
  	at org.springframework.cloud.stream.binding.InputBindingLifecycle.doStartWithBindable(InputBindingLifecycle.java:58) ~[na:na]
  	at java.util.LinkedHashMap$LinkedValues.forEach(LinkedHashMap.java:608) ~[na:na]
  	at org.springframework.cloud.stream.binding.AbstractBindingLifecycle.start(AbstractBindingLifecycle.java:57) ~[com.mycompany.consumerservice.ConsumerServiceApplication:3.2.2]
  	at org.springframework.cloud.stream.binding.InputBindingLifecycle.start(InputBindingLifecycle.java:34) ~[na:na]
  	at org.springframework.context.support.DefaultLifecycleProcessor.doStart(DefaultLifecycleProcessor.java:178) ~[na:na]
  	... 14 common frames omitted
  ```
