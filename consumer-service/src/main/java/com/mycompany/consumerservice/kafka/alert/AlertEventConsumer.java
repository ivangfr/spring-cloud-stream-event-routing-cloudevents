package com.mycompany.consumerservice.kafka.alert;

import com.mycompany.consumerservice.kafka.alert.event.EarthquakeAlert;
import com.mycompany.consumerservice.kafka.alert.event.WeatherAlert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Slf4j
@Component
public class AlertEventConsumer {

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
