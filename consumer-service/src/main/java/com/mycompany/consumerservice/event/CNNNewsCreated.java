package com.mycompany.consumerservice.event;

import lombok.Value;

@Value(staticConstructor = "of")
public class CNNNewsCreated implements NewsEvent {
    String id;
    String title;
}
