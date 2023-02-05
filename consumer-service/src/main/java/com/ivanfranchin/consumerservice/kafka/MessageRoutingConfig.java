package com.ivanfranchin.consumerservice.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.function.cloudevent.CloudEventMessageUtils;
import org.springframework.cloud.function.context.MessageRoutingCallback;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Configuration
public class MessageRoutingConfig {

    private static final Logger log = LoggerFactory.getLogger(MessageRoutingConfig.class);
    private static final Map<String, String> routingMap = new HashMap<>();

    static {
        routingMap.put("com.ivanfranchin.producerservice.kafka.alert.event.EarthquakeAlert", "earthquakeAlert");
        routingMap.put("com.ivanfranchin.producerservice.kafka.alert.event.WeatherAlert", "weatherAlert");
        routingMap.put("com.ivanfranchin.producerservice.kafka.news.event.CNNNewsCreated", "cnnNewsCreated");
        routingMap.put("com.ivanfranchin.producerservice.kafka.news.event.DWNewsCreated", "dwNewsCreated");
        routingMap.put("com.ivanfranchin.producerservice.kafka.news.event.RAINewsCreated", "raiNewsCreated");
    }

    @Bean
    public MessageRoutingCallback messageRoutingCallback() {
        return new MessageRoutingCallback() {
            @Override
            public String routingResult(Message<?> message) {
                return routingMap.getOrDefault(CloudEventMessageUtils.getType(message), "unknownEvent");
            }
        };
    }

    @Bean
    public Consumer<Message<?>> unknownEvent() {
        return message -> log.warn(LOG_TEMPLATE, "Received unknown event!", message.getHeaders(), message.getPayload());
    }

    private static final String LOG_TEMPLATE = "{}\n---\nHEADERS: {}\n...\nPAYLOAD: {}\n---";
}
