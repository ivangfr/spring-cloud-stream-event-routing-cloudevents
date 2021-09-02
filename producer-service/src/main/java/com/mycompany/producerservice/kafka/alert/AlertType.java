package com.mycompany.producerservice.kafka.alert;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AlertType {
    EARTHQUAKE("earthquake"), WEATHER("weather");

    String type;
}
