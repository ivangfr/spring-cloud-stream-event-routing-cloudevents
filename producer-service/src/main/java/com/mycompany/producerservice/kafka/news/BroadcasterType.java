package com.mycompany.producerservice.kafka.news;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BroadcasterType {
    CNN("cnn"), DW("dw"), RAI("rai");

    String type;
}
