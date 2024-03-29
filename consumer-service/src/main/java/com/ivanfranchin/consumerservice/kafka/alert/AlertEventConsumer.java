package com.ivanfranchin.consumerservice.kafka.alert;

import com.ivanfranchin.consumerservice.kafka.alert.event.EarthquakeAlert;
import com.ivanfranchin.consumerservice.kafka.alert.event.WeatherAlert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
public class AlertEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(AlertEventConsumer.class);

    @Bean
    public Consumer<Message<EarthquakeAlert>> earthquakeAlert() {
        return message -> log.info(
                LOG_TEMPLATE, "Received Earthquake alert!", message.getHeaders(), message.getPayload());
    }

    @Bean
    public Consumer<Message<WeatherAlert>> weatherAlert() {
        return message -> log.info(
                LOG_TEMPLATE, "Received Weather alert!", message.getHeaders(), message.getPayload());
    }

    private static final String LOG_TEMPLATE = "{}\n---\nHEADERS: {}\n...\nPAYLOAD: {}\n---";
}
