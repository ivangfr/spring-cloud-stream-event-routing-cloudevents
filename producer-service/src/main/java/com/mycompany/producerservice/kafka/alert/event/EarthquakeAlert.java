package com.mycompany.producerservice.kafka.alert.event;

import lombok.Value;

@Value(staticConstructor = "of")
public class EarthquakeAlert implements AlertEvent {
    String id;
    Double richterScale;
    Double epicenterLat;
    Double epicenterLon;
}
