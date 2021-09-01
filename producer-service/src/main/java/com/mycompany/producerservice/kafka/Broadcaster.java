package com.mycompany.producerservice.kafka;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Broadcaster {
    CNN("cnn"), DW("dw"), RAI("rai");

    String type;
}
