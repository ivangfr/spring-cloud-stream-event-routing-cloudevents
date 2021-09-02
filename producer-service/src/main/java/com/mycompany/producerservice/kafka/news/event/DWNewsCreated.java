package com.mycompany.producerservice.kafka.news.event;

import lombok.Value;

@Value(staticConstructor = "of")
public class DWNewsCreated implements NewsEvent {
    String id;
    String titel;
}
