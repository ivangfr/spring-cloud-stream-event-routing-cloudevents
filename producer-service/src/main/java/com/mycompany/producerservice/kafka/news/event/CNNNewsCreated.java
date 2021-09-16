package com.mycompany.producerservice.kafka.news.event;

import lombok.Value;

@Value(staticConstructor = "of")
public class CNNNewsCreated implements NewsEvent {

    String id;
    String title;
}
