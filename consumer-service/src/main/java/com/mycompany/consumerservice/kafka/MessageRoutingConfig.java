package com.mycompany.consumerservice.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.function.cloudevent.CloudEventMessageUtils;
import org.springframework.cloud.function.context.MessageRoutingCallback;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Slf4j
@Configuration
public class MessageRoutingConfig {

    private static final Map<String, String> routingMap = new HashMap<>();

    static {
        routingMap.put("com.mycompany.producerservice.kafka.alert.event.EarthquakeAlert", "earthquakeAlert");
        routingMap.put("com.mycompany.producerservice.kafka.alert.event.WeatherAlert", "weatherAlert");
        routingMap.put("com.mycompany.producerservice.kafka.news.event.CNNNewsCreated", "cnnNewsCreated");
        routingMap.put("com.mycompany.producerservice.kafka.news.event.DWNewsCreated", "dwNewsCreated");
        routingMap.put("com.mycompany.producerservice.kafka.news.event.RAINewsCreated", "raiNewsCreated");
    }

    @Bean
    public MessageRoutingCallback messageRoutingCallback() {
        return new MessageRoutingCallback() {
            @Override
            public FunctionRoutingResult routingResult(Message<?> message) {
                String functionDefinition = routingMap.getOrDefault(CloudEventMessageUtils.getType(message), "unknownEvent");
                return new FunctionRoutingResult(functionDefinition);
            }
        };
    }

    @Bean
    public Consumer<Message<?>> unknownEvent() {
        return message -> log.warn(LOG_TEMPLATE, "Received unknown event!", message.getHeaders(), message.getPayload());
    }

    private static final String LOG_TEMPLATE = "{}\n---\nHEADERS: {}\n...\nPAYLOAD: {}\n---";
}
