package com.mycompany.consumerservice.kafka.alert.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class WeatherAlert implements AlertEvent {
    private String id;
    private String message;
}
