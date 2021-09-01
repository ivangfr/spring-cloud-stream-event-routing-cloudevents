package com.mycompany.producerservice.event;

import lombok.Value;

@Value(staticConstructor = "of")
public class RAINewsCreated implements NewsEvent {
    String id;
    String titolo;
}
