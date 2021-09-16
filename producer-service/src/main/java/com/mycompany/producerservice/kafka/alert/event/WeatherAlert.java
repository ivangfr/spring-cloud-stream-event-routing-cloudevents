package com.mycompany.producerservice.kafka.alert.event;

import lombok.Value;

@Value(staticConstructor = "of")
public class WeatherAlert implements AlertEvent {

    String id;
    String message;
}
